package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testRecipe = new Recipe();
        testRecipe.setRecipeId(1L);
        testRecipe.setRecipeName("testRecipeName");
        List<Ingredient> ingredients = new ArrayList<>();
        testRecipe.setIngredients(ingredients);

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(testRecipe);
    }

    @Test
    public void creatRecipe_validInputs_success() {

        Recipe createdRecipe = recipeService.createRecipe(testRecipe);

        Mockito.verify(recipeRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testRecipe.getRecipeId(), createdRecipe.getRecipeId());
        assertEquals(testRecipe.getRecipeName(), createdRecipe.getRecipeName());
    }

    @Test
    public void getRecipeById_validInput_success() {
        given(recipeRepository.findById(Mockito.any())).willReturn(java.util.Optional.ofNullable(testRecipe));

        Recipe recipe = new Recipe();
        recipe.setRecipeId(testRecipe.getRecipeId());

        Recipe foundRecipe = recipeService.getRecipeById(recipe.getRecipeId());

        assertEquals(testRecipe.getRecipeId(), foundRecipe.getRecipeId());
    }

}
