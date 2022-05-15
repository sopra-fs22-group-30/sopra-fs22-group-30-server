package ch.uzh.ifi.hase.soprafs22.service;


import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
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

import java.util.*;

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
            ingredientRepository.saveAndFlush(ingredient);
            ingredient.setRecipeId(newRecipe.getRecipeId());
            ingredientRepository.flush();
        }

        recipeRepository.saveAndFlush(newRecipe);

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

                //first delete ingredients to avoid foreign key problem
                for (Ingredient ingredient : checkRecipe.get().getIngredients()) {
                    ingredient.setRecipeId(null);
                }
                //second delete likes to avoid foreign key problem
                for (String username : checkRecipe.get().getLikedUser()) {
                    Optional<User> checkUser = Optional.ofNullable(userRepository.findByUsername(username));
                    User user_ = checkUser.get();
                    List<Recipe> likeList = user_.getLikeList();
                    likeList.removeIf(recipe -> (recipe.getRecipeId()==recipeId));
                    userRepository.saveAndFlush(user_);
                }
                //remove recipe
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
                recipeToBeUpdated.setPictureLocation(newRecipe.getPictureLocation());

                for (Ingredient ingredient : recipeToBeUpdated.getIngredients()) {
                    ingredient.setRecipeId(null);
                }

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
    public boolean likeAndUnlike(Long userId, Long recipeId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (user.isPresent() && recipe.isPresent()) {
            Recipe recipe_ = recipe.get();
            User user_ = user.get();
            List<String> likedUser = recipe_.getLikedUser();
            Long likesNum = recipe_.getLikesNum();
            String username = user_.getUsername();
            List<Recipe> userLikeList = user_.getLikeList();
            if (likedUser.contains(username)) {//to unlike
                //remove user
                likedUser.remove(username);
                recipe_.setLikedUser(likedUser);
                //decrease likesNum
                likesNum = likesNum - 1L;
                recipe_.setLikesNum(likesNum);
                //remove recipe from userLikeList
                userLikeList.remove(recipe_);
                //user_.setLikeList(userLikeList);
                userRepository.saveAndFlush(user_);
                recipeRepository.saveAndFlush(recipe_);
                return false;
            }
            else {//to like
                //add user
                likedUser.add(username);
                recipe_.setLikedUser(likedUser);
                //increment likesNum
                likesNum = likesNum + 1L;
                recipe_.setLikesNum(likesNum);
                //add recipe from userLikeList
                userLikeList.add(recipe_);
                //user_.setLikeList(userLikeList);
                userRepository.saveAndFlush(user_);
                recipeRepository.saveAndFlush(recipe_);
                return true;
            }
        }else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or recipe was not found!");
        }
    }

    public boolean likeOrUnlike(Long userId, Long recipeId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Recipe> recipe = recipeRepository.findById(recipeId);
        if (user.isPresent() && recipe.isPresent()) {
            Recipe recipe_ = recipe.get();
            User user_ = user.get();
            List<String> likedUser = recipe_.getLikedUser();
            String username = user_.getUsername();
            if (likedUser.contains(username)){
                return true;
            } else {
                return false;
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User or recipe was not found!");
        }
    }

    // find recipes by filter
    public List<Recipe> getRecipesByFilter(Cuisine filter) {
        List<Recipe> recipesByFilter = new ArrayList<>();
        for (Recipe recipe : recipeRepository.findAll()) {
            if (recipe.getCuisine().equals(filter)) {
                recipesByFilter.add(recipe);
            }
        }
        return recipesByFilter;
    }

}
