package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePostDTO;
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
}

