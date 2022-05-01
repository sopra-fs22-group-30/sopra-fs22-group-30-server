package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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

    @BeforeEach
    public void setup() {
        recipeRepository.deleteAll();
        ingredientRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void creatRecipe_validInputs_success() {
        //given
        Recipe testRecipe = new Recipe();
        testRecipe.setAuthorId(1L);
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

        User testUser = new User();
        testUser.setUsername("testName");
        testUser.setPassword("1234565");

        User createdUser = userService.createUser(testUser);

        //when
        Recipe createdRecipe = recipeService.createRecipe(testRecipe);

        //then
        assertEquals(testRecipe.getRecipeId(), createdRecipe.getRecipeId());
        assertEquals(testRecipe.getRecipeName(), createdRecipe.getRecipeName());
    }

    @Test
    public void test_editRecipe() {
        //given
        Recipe testRecipe = new Recipe();
        testRecipe.setAuthorId(1L);
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

        User testUser = new User();
        testUser.setUsername("testName");
        testUser.setPassword("1234565");

        User createdUser = userService.createUser(testUser);
        Recipe createdRecipe = recipeService.createRecipe(testRecipe);

        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeId(1L);
        newRecipe.setCost(2L);
        newRecipe.setRecipeName("newRecipeName");
        List<Ingredient> newIngredients = new ArrayList<>();
        Ingredient newIngredient = new Ingredient();
        newIngredient.setName("beef");
        newIngredient.setAmount(500);
        ingredients.add(newIngredient);
        newRecipe.setIngredients(newIngredients);
        newRecipe.setContent("new content");
        newRecipe.setTimeConsumed(1L);
        newRecipe.setCuisine(Cuisine.Algerian);
        newRecipe.setPortion(1);

        recipeService.editRecipe(1L,1L,newRecipe);

        Recipe editedRecipe = new Recipe();
        if (recipeRepository.findById(1L).isPresent()){
            editedRecipe = recipeRepository.findById(1L).get();
        } else {
            assertThrows(ResponseStatusException.class, () -> recipeService.getRecipeById(1L));
        }
        assertEquals(editedRecipe.getRecipeName(), newRecipe.getRecipeName());
        assertEquals(editedRecipe.getCost(), newRecipe.getCost());
    }
}
