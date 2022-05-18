package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class RecipeServiceIntegrationTest {

    @Qualifier("recipeRepository")
    @Autowired
    private RecipeRepository recipeRepository;

    @Qualifier("ingredientRepository")
    @Autowired
    private IngredientRepository ingredientRepository;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    Recipe testRecipe;
    Recipe createdRecipe;
    User testUser;
    User createdUser;

    @BeforeEach
    public void setup() {
        ingredientRepository.deleteAll();
        recipeRepository.deleteAll();
        userRepository.deleteAll();

        testUser = new User();
        testUser.setUsername("testName");
        testUser.setPassword("1234565");

        createdUser = userService.createUser(testUser);

        testRecipe = new Recipe();
        testRecipe.setAuthorId(createdUser.getId());
        testRecipe.setContent("content");
        testRecipe.setRecipeName("testRecipeName");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("eggs");
        ingredient.setAmount(50);
        ingredients.add(ingredient);
        testRecipe.setIngredients(ingredients);
        testRecipe.setCost(1L);
        testRecipe.setTimeConsumed(1L);
        testRecipe.setCuisine(Cuisine.Algerian);
        testRecipe.setPortion(1);

        createdRecipe = recipeService.createRecipe(testRecipe);
    }

    @AfterEach
    public void finish() {
        recipeService.likeAndUnlike(testUser.getId(),testRecipe.getRecipeId());

        ingredientRepository.deleteAll();
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void creatRecipe_validInputs_success() {
        assertEquals(testRecipe.getRecipeName(), createdRecipe.getRecipeName());
    }

    @Test
    public void getRecipeById_success() {
        Recipe foundRecipe = recipeService.getRecipeById(createdRecipe.getRecipeId());

        assertEquals(foundRecipe.getRecipeName(), testRecipe.getRecipeName());
    }

    @Test
    public void getRecipeById_fail () {
        assertThrows(ResponseStatusException.class, () -> recipeService.getRecipeById(1000L));
    }

    @Test
    public void test_editRecipe() {

        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeId(createdRecipe.getRecipeId());
        newRecipe.setCost(2L);
        newRecipe.setRecipeName("newRecipeName");
        List<Ingredient> newIngredients = new ArrayList<>();
        Ingredient newIngredient = new Ingredient();
        newIngredient.setName("beef");
        newIngredient.setAmount(500);
        newIngredients.add(newIngredient);
        newRecipe.setIngredients(newIngredients);
        newRecipe.setContent("new content");
        newRecipe.setTimeConsumed(1L);
        newRecipe.setCuisine(Cuisine.Algerian);
        newRecipe.setPortion(1);

        recipeService.editRecipe(createdUser.getId(),createdRecipe.getRecipeId(),newRecipe);

        Recipe editedRecipe = new Recipe();
        if (recipeRepository.findById(createdRecipe.getRecipeId()).isPresent()){
            editedRecipe = recipeRepository.findById(createdRecipe.getRecipeId()).get();
        } else {
            assertThrows(ResponseStatusException.class, () -> recipeService.getRecipeById(1L));
        }
        assertEquals(editedRecipe.getRecipeName(), newRecipe.getRecipeName());
        assertEquals(editedRecipe.getCost(), newRecipe.getCost());
    }

    @Test
    public void likeOrUnlike_Like() {
        assertTrue(recipeService.likeOrUnlike(createdUser.getId(),createdRecipe.getRecipeId()));
    }
}
