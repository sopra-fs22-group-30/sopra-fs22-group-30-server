package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
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
        for (String username: newParty.getPartyAttendentsList()) {
            User checkUser = userRepository.findByUsername(username);
            if (checkUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Username may not exist!!");
            }
        }
        Optional<Recipe> recipeToCheck = recipeRepository.findById(newParty.getRecipeUsedId());
        if (!recipeToCheck.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe may not exist!!");
        }


        newParty.setCreationDate(new Date());
        List<String> ingredientList = new ArrayList<>() {
        };
        for (Ingredient ingredient : recipeToCheck.get().getIngredients()) {
            ingredientList.add(ingredient.getName());
        }
        newParty.setIngredients(ingredientList);
        Integer size = newParty.getPartyAttendentsList().size();
        newParty.setPartyAttendentsNum(size);


        for(String username: newParty.getPartyAttendentsList()) {
            User attendent = userRepository.findByUsername(username);
            attendent.addJoinParties(newParty.getPartyName());
            userRepository.saveAndFlush(attendent);
        }

        partyRepository.saveAndFlush(newParty);
        return newParty;

    }

    // get one party detail
    public Party getPartyById(Long userId, Long partyId) {
        Optional<Party> partyToGet = partyRepository.findById(partyId);
        if (!partyToGet.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        Optional<User> userToCheck = userRepository.findById(userId);
        if (userId != partyToGet.get().getPartyHostId() && !userToCheck.get().getJoinParties().contains(partyToGet.get().getPartyName()) ){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not in this party");
        }
        return partyToGet.get();
    }

    // edit party
    public Party editParty(Long userId, Long partyId, Party newParty) {
        Optional<Party> partyToUpdate = partyRepository.findById(partyId);
        if (!partyToUpdate.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        else if (partyToUpdate.get().getPartyHostId() != userId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot edit party");
        }

        for (String username : newParty.getPartyAttendentsList()) {
            User checkUser = userRepository.findByUsername(username);
            if (checkUser == null) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The user you invited may not exist!");
            }
        }

        partyToUpdate.get().setPartyIntro(newParty.getPartyIntro());
        partyToUpdate.get().setPlace(newParty.getPlace());
        partyToUpdate.get().setTime(newParty.getTime());
        if (partyToUpdate.get().getPartyAttendentsList() != newParty.getPartyAttendentsList()) {
            Integer newSize = newParty.getPartyAttendentsList().size();
            partyToUpdate.get().setPartyAttendentsNum(newSize);
            for (String username : newParty.getPartyAttendentsList()) {
                User attendent = userRepository.findByUsername(username);
                if (!attendent.getJoinParties().contains(partyToUpdate.get().getPartyName())) {
                    attendent.addJoinParties(newParty.getPartyName());
                    userRepository.saveAndFlush(attendent);
                }
            }


            partyRepository.saveAndFlush(partyToUpdate.get());

        }
        return newParty;
    }

    public void deleteParty(Long userId, Long partyId){
        Optional<Party> partyToDelete = partyRepository.findById(partyId);
        if (!partyToDelete.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        else if (partyToDelete.get().getPartyHostId() != userId) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "You cannot delete party");
        }
        partyRepository.deleteById(partyId);
    }


    public void quitParty(Long userId, Long partyId){
        Optional<Party> partyChecked= partyRepository.findById(partyId);
        Optional<User> userChecked = userRepository.findById(userId);
        if (!partyChecked.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Party may not exist!!");
        }
        List<String> AttendantsList = partyChecked.get().getPartyAttendentsList();
        if (!userChecked.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This user does not exist!!");
        }
        String quittingUserName = userChecked.get().getUsername();
        if (!AttendantsList.contains(quittingUserName)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not attendant of this party");
        }
        AttendantsList.remove(quittingUserName);
        Party partyToQuit = partyChecked.get();
        partyToQuit.setPartyAttendentsList(AttendantsList);
        partyToQuit.setPartyAttendentsNum(AttendantsList.size());
        partyRepository.saveAndFlush(partyToQuit);

        User userQuitting = userChecked.get();
        Set<String> joinedParties = userQuitting.getJoinParties();
        joinedParties.remove(partyToQuit.getPartyName());
        userQuitting.setJoinParties(joinedParties);
        userRepository.saveAndFlush(userQuitting);
    }
}
