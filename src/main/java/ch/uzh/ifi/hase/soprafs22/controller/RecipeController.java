package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RecipeController {

    private final RecipeService recipeService;

    RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }


    // test method (delete when complete)
    @GetMapping("/recipes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RecipeGetDTO> getAllRecipes() {
        // fetch all users in the internal representation
        List<Recipe> recipes = recipeService.getRecipes();
        List<RecipeGetDTO> recipeGetDTOs = new ArrayList<>();


        // convert each user to the API representation
        for (Recipe recipe : recipes) {
            recipeGetDTOs.add(DTOMapper.INSTANCE.convertEntityToRecipeGetDTO(recipe));
        }
        return recipeGetDTOs;
    }

    // get one recipe
    @GetMapping("/recipes/{recipeId}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public RecipeGetDTO recipeGetDTO (@PathVariable("recipeId") Long recipeId) {
        Recipe recipe = recipeService.getRecipeById(recipeId);
        return DTOMapper.INSTANCE.convertEntityToRecipeGetDTO(recipe);
    }


    // create new recipe
    @PostMapping("/recipes")
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public RecipeGetDTO createRecipe(@RequestBody RecipePostDTO recipePostDTO) {
        // convert API recipe to internal representation
        Recipe recipeInput = DTOMapper.INSTANCE.convertRecipePostDTOtoEntity(recipePostDTO);

        // create recipe
        Recipe createdRecipe = recipeService.createRecipe(recipeInput);

        // convert internal representation of user back to API
        return DTOMapper.INSTANCE.convertEntityToRecipeGetDTO(createdRecipe);
    }


    // delete own recipe
    @DeleteMapping("/users/{userId}/recipes/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void deleteRecipe(@PathVariable Long userId, @PathVariable Long recipeId) {
        recipeService.deleteRecipe(userId, recipeId);
    }

    // edit recipe
    @PutMapping("/users/{userId}/recipes/{recipeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    public void editRecipe(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId, @RequestBody RecipePutDTO recipePutDTO) {

        // updates the user identified by the given ID with the given data by the client
        Recipe recipeInput = DTOMapper.INSTANCE.convertRecipePutDTOtoEntity(recipePutDTO);
        recipeService.editRecipe(userId, recipeId, recipeInput);

    }

    // like and unlike a recipe
    @PostMapping("/users/{userId}/recipes/{recipeId}/likes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean likeAndUnlike(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId) {

        return recipeService.likeAndUnlike(userId, recipeId);
    }

    // like and unlike a recipe
    @GetMapping("/users/{userId}/recipes/{recipeId}/likes")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public boolean likeOrUnlike(@PathVariable("userId") Long userId, @PathVariable("recipeId") Long recipeId) {

        return recipeService.likeOrUnlike(userId, recipeId);
    }

    @GetMapping("/recipes/filter/{filter}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public List<RecipeGetDTO> getRecipesByFilter(@PathVariable("filter") Cuisine filter) {
        // fetch all users in the internal representation
        List<Recipe> recipes = recipeService.getRecipesByFilter(filter);
        List<RecipeGetDTO> recipeGetDTOs = new ArrayList<>();


        // convert each user to the API representation
        for (Recipe recipe : recipes) {
            recipeGetDTOs.add(DTOMapper.INSTANCE.convertEntityToRecipeGetDTO(recipe));
        }
        return recipeGetDTOs;
    }

}

