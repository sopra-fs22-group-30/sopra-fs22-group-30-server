package ch.uzh.ifi.hase.soprafs22.service;


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

@Service
@Transactional
public class RecipeService {
    private final Logger log = LoggerFactory.getLogger(RecipeService.class);

    private final RecipeRepository recipeRepository;
    private final UserRepository userRepository;

    @Autowired
    public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository, UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.userRepository = userRepository;
    }

    public List<Recipe> getRecipes() {
        return this.recipeRepository.findAll();
    }

    // create new recipe
    public Recipe createRecipe(Recipe newRecipe) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newRecipe.setCreationDate(new Date());
//        Optional<User> author = userRepository.findById(newRecipe.getAuthorId());
        newRecipe.setLikeNum(0L);
        newRecipe = recipeRepository.save(newRecipe);
        recipeRepository.flush();

        log.debug("Created Information for User: {}", newRecipe);
        return newRecipe;
    }

    // get recipe by id
    public Recipe getRecipeById(Long recipeId) {
        Optional<Recipe> checkRecipe = recipeRepository.findById(recipeId);
        if (checkRecipe.isPresent()) {
            return checkRecipe.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe was not found!");
        }
    }

    // delete own recipe
    public void deleteRecipe(Long userId, Long recipeId) {
        Optional<Recipe> checkRecipe = recipeRepository.findById(recipeId);
        if (checkRecipe.isPresent()) {
            Long authorId = checkRecipe.get().getAuthorId();
            if (userId != authorId) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fail to delete this recipe because the user is not the author");
            }
            else{
                recipeRepository.deleteById(recipeId);
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!");
        }


    }


}
