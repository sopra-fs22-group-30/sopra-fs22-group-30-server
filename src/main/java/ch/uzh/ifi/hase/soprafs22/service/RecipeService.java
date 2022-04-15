package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
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
    private final IngredientRepository ingredientRepository;

    private final UserRepository userRepository;

    @Autowired
    public RecipeService(@Qualifier("recipeRepository") RecipeRepository recipeRepository, @Qualifier("ingredientRepository") IngredientRepository ingredientRepository, @Qualifier("userRepository") UserRepository userRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
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
        newRecipe.setLikesNum(0L);
        newRecipe = recipeRepository.save(newRecipe);

        for (Ingredient ingredient : newRecipe.getIngredients()) {
            ingredient.setRecipeId(newRecipe.getRecipeId());
            ingredientRepository.save(ingredient);
        }

        ingredientRepository.save(newRecipe.getIngredients().get(0));
        recipeRepository.flush();

        log.debug("Created Information for User: {}", newRecipe);

        return newRecipe;
    }


    // get recipe by id
    public Recipe getRecipeById(Long recipeId) {
        Optional<Recipe> checkRecipe = recipeRepository.findById(recipeId);

        if (checkRecipe.isPresent()) {
//            System.out.println("hi");
//            System.out.println(ingredientRepository.findAll());
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
                for (Ingredient ingredient : checkRecipe.get().getIngredients()) {
                    ingredient.setRecipeId(null);
                }
                recipeRepository.deleteById(recipeId);
            }
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!");
        }


    }

    //edit recipe
    public void editRecipe(Long userId, Long recipeId, Recipe newRecipe) {
        // saves the given entity but data is only persisted in the database once
        // flush() is called
        Optional<Recipe> checkRecipe = recipeRepository.findById(recipeId);
        if (checkRecipe.isPresent()) {
            if (userId != checkRecipe.get().getAuthorId()) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Fail to delete this recipe because the user is not the author");
            }else{
                Recipe recipeToBeUpdated = getRecipeById(recipeId);
                recipeToBeUpdated.setRecipeName(newRecipe.getRecipeName());
                recipeToBeUpdated.setCuisine(newRecipe.getCuisine());
                recipeToBeUpdated.setTimeConsumed(newRecipe.getTimeConsumed());
                recipeToBeUpdated.setContent(newRecipe.getContent());
                recipeToBeUpdated.setCost(newRecipe.getCost());
                recipeToBeUpdated.setPortion(newRecipe.getPortion());

                for (Ingredient ingredient : recipeToBeUpdated.getIngredients()) {
                    ingredient.setRecipeId(null);
                }
//                System.out.println(newRecipe.getIngredients());

//                newRecipe = recipeRepository.saveAndFlush(newRecipe);
                for (Ingredient ingredient : newRecipe.getIngredients()) {
                    ingredient.setRecipeId(recipeId);
                    ingredientRepository.save(ingredient);
                }

                recipeRepository.saveAndFlush(recipeToBeUpdated);
//              TODO: maybe delete ingredients which recipeId is null in ingredientRepository.
            }
        }else{
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Recipe was not found!");
        }


    }

    // like and unlike
    public Boolean likeAndUnlike(Long userId, Long recipeId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (user.isPresent() && recipe.isPresent()) {
            if (recipe.get().getLikedUser().contains(user.get().getUsername())) {
                recipe.get().getLikedUser().remove(user.get().getUsername());
                recipe.get().setLikesNum(recipe.get().getLikesNum() - 1L);
                user.get().getLikeList().remove(recipe.get());
                recipeRepository.saveAndFlush(recipe.get());
                userRepository.saveAndFlush(user.get());
                return Boolean.FALSE;
            }
            else {
                recipe.get().getLikedUser().add(user.get().getUsername());
                recipe.get().setLikesNum(recipe.get().getLikesNum() + 1L);
                user.get().getLikeList().add(recipe.get());
                recipeRepository.saveAndFlush(recipe.get());
                userRepository.saveAndFlush(user.get());
                return Boolean.TRUE;
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or recipe was not found!");
        }
    }




}
