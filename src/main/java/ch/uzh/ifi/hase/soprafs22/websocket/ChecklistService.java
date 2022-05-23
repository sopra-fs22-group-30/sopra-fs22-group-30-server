package ch.uzh.ifi.hase.soprafs22.websocket;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistGetDTO;
import ch.uzh.ifi.hase.soprafs22.websocket.dtoWS.ChecklistMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ChecklistService {

    private final IngredientRepository ingredientRepository;
    private final UserRepository userRepository;

    @Autowired
    public ChecklistService(@Qualifier("ingredientRepository") IngredientRepository ingredientRepository, UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
    }

    public void storeAndConvert(ChecklistMessageDTO checklistMessageDTO) {
        Long takerId = checklistMessageDTO.getTakerId();
        Long ingredientId =  checklistMessageDTO.getIngredientId();
        Optional<User> checkedUser = userRepository.findById(takerId);
        if (checkedUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found!");
        }
        String taker = checkedUser.get().getUsername();
        Optional<Ingredient> checkedIngredient = ingredientRepository.findById(ingredientId);
        if (checkedIngredient.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ingredient not found!");
        }

        //set the taker of the ingredient in a certain party
        checkedIngredient.get().setTakerId(takerId);
        checkedIngredient.get().setTakerName(taker);

        ingredientRepository.saveAndFlush(checkedIngredient.get());

    }

    public List<ChecklistGetDTO> getChecklistInParty(Long partyId) {
        List<ChecklistGetDTO> checklist = new ArrayList<>();
        List<Ingredient> ingredientsFound =  ingredientRepository.findByPartyId(partyId);
        for (Ingredient ingredient: ingredientsFound){
            ChecklistGetDTO checklistGetDTO = new ChecklistGetDTO();
            checklistGetDTO.setPartyId(ingredient.getPartyId());
            checklistGetDTO.setIngredientId(ingredient.getIngredientId());
            checklistGetDTO.setIngredientName(ingredient.getName());
            Optional<User> user = userRepository.findById(ingredient.getTakerId());
            user.ifPresent(value -> checklistGetDTO.setTaker(value.getUsername()));
            checklist.add(checklistGetDTO);
        }
        return checklist;
    }
}
