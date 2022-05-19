package ch.uzh.ifi.hase.soprafs22.service;

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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

public class RecipeServiceTest {
    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private RecipeService recipeService;

    @Mock
    private UserService userService;

    private Recipe testRecipe;
    private User testUser;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        // given
        testRecipe = new Recipe();
        testRecipe.setRecipeId(1L);
        testRecipe.setRecipeName("testRecipeName");
        testRecipe.setAuthorId(1L);
        List<String> likedUsers = new ArrayList<>();
        likedUsers.add("testUsername");
        testRecipe.setLikedUser(likedUsers);
        testRecipe.setLikesNum(1L);
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("eggs");
        ingredient.setAmount(50);
        ingredients.add(ingredient);
        testRecipe.setIngredients(ingredients);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        List<Recipe> userLikedList = new ArrayList<>();
        userLikedList.add(testRecipe);
        testUser.setLikeList(userLikedList);

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(testRecipe);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }


    @Test
    public void creatRecipe_validInputs_success() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(userService.getUserById(Mockito.any())).thenReturn(testUser);

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

    @Test
    public void test_editRecipe_success() {
        Recipe newRecipe = new Recipe();
        newRecipe.setRecipeId(1L);
        newRecipe.setRecipeName("newName");
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("beef");
        ingredient.setAmount(50);
        ingredients.add(ingredient);
        newRecipe.setIngredients(ingredients);

        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(newRecipe);
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testRecipe));

        recipeService.editRecipe(1L,1L,newRecipe);

        assertEquals(testRecipe.getRecipeName(), newRecipe.getRecipeName());
    }

    @Test
    public void test_likeAndUnlike_Unlike() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testRecipe));

        assertFalse(recipeService.likeAndUnlike(1L, 1L));
    }

    @Test
    public void test_likeAndUnlike_Like() {
        User newUser = new User();
        newUser.setUsername("new");
        newUser.setPassword("111");
        List<Recipe> userLikedList = new ArrayList<>();
        newUser.setLikeList(userLikedList);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(newUser);

        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(newUser));
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testRecipe));

        assertTrue(recipeService.likeAndUnlike(newUser.getId(), 1L));
    }

    @Test
    public void test_LikeOrUnlike() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testRecipe));

        assertTrue(recipeService.likeOrUnlike(1L, 1L));
    }

    @Test
    public void deleteRecipe_success() {
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testRecipe));
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);

        recipeService.deleteRecipe(1L, 1L);
        Mockito.verify(recipeRepository).deleteById(1L);
    }

}
