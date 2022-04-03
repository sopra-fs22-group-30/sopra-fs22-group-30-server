package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Gender;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
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

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class RecipeService {
    private final Logger log = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;

    @Autowired
    public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    public List<Recipe> getRecipes() {
        return this.recipeRepository.findAll();
    }

    // create new recipe
    public Recipe createRecipe(Recipe newRecipe) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newRecipe.setCreationDate(new Date());
        newRecipe = recipeRepository.save(newRecipe);
        recipeRepository.flush();

        log.debug("Created Information for User: {}", newRecipe);
        return newRecipe;
    }



}
