package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.hibernate.query.criteria.internal.expression.function.AggregationFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

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
        List<Ingredient> ingredientsList = recipeToCheck.get().getIngredients();
        newParty.setIngredients(ingredientsList);
        Integer size = newParty.getPartyAttendentsList().size();
        newParty.setPartyAttendentsNum(size);

        partyRepository.saveAndFlush(newParty);
        return newParty;

    }
}
