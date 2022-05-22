package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.InvitationNotificationDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@Transactional
public class PartyService {
    private final Logger log = LoggerFactory.getLogger(PartyService.class);

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;
    private final PartyRepository partyRepository;
    private final UserRepository userRepository;


    @Autowired
    public PartyService(@Qualifier("recipeRepository") RecipeRepository recipeRepository, @Qualifier("ingredientRepository") IngredientRepository ingredientRepository, @Qualifier("partyRepository") PartyRepository partyRepository, @Qualifier("userRepository") UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
        this.partyRepository = partyRepository;
        this.userRepository = userRepository;
    }

    public List<Party> getParties() {
        return this.partyRepository.findAll();
    }

    // create new party
    public Party createParty(Party newParty) {
        //set creationDate
        newParty.setCreationDate(new Date());
        //set recipeUsedName
        String recipeUsedName = recipeRepository.findById(newParty.getRecipeUsedId()).get().getRecipeName();
        newParty.setRecipeUsedName(recipeUsedName);
        for (String username: newParty.getPartyAttendantsList()) {
            User checkUser = userRepository.findByUsername(username);
            if (checkUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username may not exist!!");
            }
        }
        Optional<Recipe> recipeToCheck = recipeRepository.findById(newParty.getRecipeUsedId());
        if (recipeToCheck.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe may not exist!!");
        }

        //set party attendants number
        int size = newParty.getPartyAttendantsList().size();
        newParty.setPartyAttendantsNum(size);

        newParty=partyRepository.saveAndFlush(newParty);

        for(String username: newParty.getPartyAttendantsList()) {
            User attendant = userRepository.findByUsername(username);
            attendant.addJoinParties(newParty.getPartyId());
            userRepository.saveAndFlush(attendant);
        }

        //set party ingredient list equal to recipe ingredient list
        List<Ingredient> ingredientList = new ArrayList<>();
        for (Ingredient ingredient: recipeToCheck.get().getIngredients()){
            Ingredient ingredientCopied = new Ingredient();
            ingredientCopied.setName(ingredient.getName());
            ingredientCopied.setAmount(ingredient.getAmount());
            ingredientCopied.setPartyId(newParty.getPartyId());
            ingredientCopied.setRecipeId(null);
            ingredientList.add(ingredientCopied);
        }

        newParty.setIngredients(ingredientList);
        partyRepository.saveAndFlush(newParty);
        return newParty;
    }

    //get a list of parties of a specific user
    public List<Party> getPartiesAUserIsIn(Long userId){
        Optional<User> foundUser = userRepository.findById(userId);
        //find user
        if(foundUser.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!!");
        }
        //find parties this user host
        List<Party> parties = new ArrayList<>(foundUser.get().getHostParties());
        //find parties this user attends
        for (Long partyId: foundUser.get().getJoinParties()) {
            Optional<Party> foundParty = partyRepository.findById(partyId);
            foundParty.ifPresent(parties::add);
        }
        return parties;
    }

    public List<Long> findNewAttendantsAdded(Long partyId, Party partyToUpdate){
        //initialize object
        List<Long> newAttendants = new ArrayList<>();
        //find old list of attendants
        List<String> oldPartyAttendantsList = partyRepository.findById(partyId).get().getPartyAttendantsList();
        //check new list of attendants
        List<String> newPartyAttendantsList = partyToUpdate.getPartyAttendantsList();
        //if user is newly added. find his or her userId and add it to the list
        for (String username: newPartyAttendantsList){
            if (!oldPartyAttendantsList.contains(username)){
                newAttendants.add(userRepository.findByUsername(username).getId());
            }
        }
        return newAttendants;
    }


    // get one party detail
    public Party getPartyById(Long userId, Long partyId) {
        Optional<Party> partyToGet = partyRepository.findById(partyId);
        if (partyToGet.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        Optional<User> userToCheck = userRepository.findById(userId);
        if (!userId.equals(partyToGet.get().getPartyHostId()) && !userToCheck.get().getJoinParties().contains(partyToGet.get().getPartyId()) ){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not in this party");
        }
        return partyToGet.get();
    }

    // edit party
    public Party editParty(Long partyId, Party newParty) {
        Optional<Party> partyToUpdate = partyRepository.findById(partyId);
        partyToUpdate.get().setPartyIntro(newParty.getPartyIntro());
        partyToUpdate.get().setPlace(newParty.getPlace());
        partyToUpdate.get().setTime(newParty.getTime());
        partyToUpdate.get().setPartyName(newParty.getPartyName());
        // check whether the attendants list is changed
        if (partyToUpdate.get().getPartyAttendantsList() != newParty.getPartyAttendantsList()) {
            Integer newSize = newParty.getPartyAttendantsList().size();
            partyToUpdate.get().setPartyAttendantsNum(newSize);
            // for those attendants in the old version
            for (String username : partyToUpdate.get().getPartyAttendantsList()) {
                User attendant = userRepository.findByUsername(username);
                // if they are not added to the new version
                if (!newParty.getPartyAttendantsList().contains(username)) {
                    attendant.deleteJoinParties(partyId);
                    userRepository.saveAndFlush(attendant);
                    //if this user is not attending anymore, his/her ingredients will be set to null
                    for (Ingredient ingredients : partyToUpdate.get().getIngredients()) {
                        String originalTaker = ingredients.getTakerName();
                        if ( originalTaker!= null){
                            if (originalTaker.equals(username)) {
                                ingredients.setTakerId(null);
                                ingredients.setTakerName(null);
                                ingredientRepository.saveAndFlush(ingredients);
                            }
                        }

                    }
                }
            }
            // for those who are newly added to the party
            for (String username : newParty.getPartyAttendantsList()) {
                User attendant = userRepository.findByUsername(username);
                if (!attendant.getJoinParties().contains(partyToUpdate.get().getPartyId())) {
                    attendant.addJoinParties(partyToUpdate.get().getPartyId());
                    userRepository.saveAndFlush(attendant);
                }
            }
            partyToUpdate.get().setPartyAttendantsList(newParty.getPartyAttendantsList());



            partyRepository.saveAndFlush(partyToUpdate.get());

        }
        return newParty;
    }

    public void checkEditPartyConditions(Long userId, Long partyId, Party newParty) {
        //special cases:
        Optional<Party> partyToUpdate = partyRepository.findById(partyId);
        if (partyToUpdate.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        else if (!partyToUpdate.get().getPartyHostId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot edit party");
        }
        for (String username : newParty.getPartyAttendantsList()) {
            User checkUser = userRepository.findByUsername(username);
            if (checkUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user you invited may not exist!");
            }
        }
    }

    public void deleteParty(Long userId, Long partyId){
        Optional<Party> partyToDelete = partyRepository.findById(partyId);
        if (partyToDelete.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        else if (!partyToDelete.get().getPartyHostId().equals(userId)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot delete party");
        }
        List<Ingredient> partyIngredients = ingredientRepository.findByPartyId(partyId);
        ingredientRepository.deleteAll(partyIngredients);
        partyRepository.deleteById(partyId);
    }


    public void quitParty(Long userId, Long partyId){
        Optional<Party> partyChecked= partyRepository.findById(partyId);
        Optional<User> userChecked = userRepository.findById(userId);
        if (partyChecked.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        List<String> AttendantsList = partyChecked.get().getPartyAttendantsList();
        if (userChecked.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist!!");
        }
        String quittingUserName = userChecked.get().getUsername();
        if (!AttendantsList.contains(quittingUserName)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not attendant of this party");
        }
        AttendantsList.remove(quittingUserName);
        Party partyToQuit = partyChecked.get();
        partyToQuit.setPartyAttendantsList(AttendantsList);
        partyToQuit.setPartyAttendantsNum(AttendantsList.size());
        partyRepository.saveAndFlush(partyToQuit);

        User userQuitting = userChecked.get();
        Set<Long> joinedParties = userQuitting.getJoinParties();
        joinedParties.remove(partyToQuit.getPartyId());
        userQuitting.setJoinParties(joinedParties);
        userRepository.saveAndFlush(userQuitting);

        for (Ingredient ingredients : partyChecked.get().getIngredients()) {
            if (ingredients.getTakerName() != null && ingredients.getTakerName().equals(userChecked.get().getUsername())) {
                ingredients.setTakerId(null);
                ingredients.setTakerName(null);
                ingredientRepository.saveAndFlush(ingredients);
            }
        }




    }

    public InvitationNotificationDTO prepareNotification (Long userId,Long partyId,Party partyToUpdate){
        InvitationNotificationDTO notification = new InvitationNotificationDTO();
        notification.setPartyId(partyId);
        notification.setPartyName(partyToUpdate.getPartyName());
        notification.setHostName(userRepository.findById(userId).get().getUsername());
        return notification;
    }
}
