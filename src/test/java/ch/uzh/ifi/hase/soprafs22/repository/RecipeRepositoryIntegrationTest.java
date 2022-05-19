package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
public class RecipeRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RecipeRepository recipeRepository;

    User testUser;
    Recipe testRecipe;
    Recipe testRecipe1;

    @BeforeEach
    public void setup() {
        // given
        testUser = new User();
        testUser.setUsername("testName");
        testUser.setPassword("1234565");
        testUser.setToken("1");

        entityManager.persist(testUser);
        entityManager.flush();

        testRecipe = new Recipe();
        testRecipe.setAuthorId(testUser.getId());
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
        testRecipe.setLikesNum(0L);

        entityManager.persist(testRecipe);
        entityManager.flush();

        testRecipe1 = new Recipe();
        testRecipe1.setAuthorId(testUser.getId());
        testRecipe1.setContent("content1");
        testRecipe1.setRecipeName("testRecipeName1");
        List<Ingredient> ingredients1 = new ArrayList<>();
        Ingredient ingredient1 = new Ingredient();
        ingredient1.setName("beef");
        ingredient1.setAmount(50);
        ingredients.add(ingredient1);
        testRecipe1.setIngredients(ingredients1);
        testRecipe1.setCost(2L);
        testRecipe1.setTimeConsumed(2L);
        testRecipe1.setCuisine(Cuisine.Algerian);
        testRecipe1.setPortion(2);
        testRecipe1.setLikesNum(0L);

        entityManager.persist(testRecipe1);
        entityManager.flush();
    }

    @Test
    public void findByID_success() {

        // when
        Optional<Recipe> checkRecipe = recipeRepository.findById(testRecipe.getRecipeId());
        Recipe found = null;
        if (checkRecipe.isPresent()) {
            found = checkRecipe.get();
        }
        // then
        assert found != null;
        assertNotNull(found.getRecipeId());
        assertEquals(found.getRecipeName(), testRecipe.getRecipeName());
        assertEquals(found.getRecipeId(), testRecipe.getRecipeId());
    }

    @Test
    public void findAll() {

        // when
        List<Recipe> found = recipeRepository.findAll();

        // then
        assertNotNull(found.get(0).getRecipeId());
        assertEquals(found.get(0).getRecipeId(), testRecipe.getRecipeId());
        assertEquals(found.get(0).getRecipeName(), testRecipe.getRecipeName());

        assertNotNull(found.get(1).getRecipeId());
        assertEquals(found.get(1).getRecipeId(), testRecipe1.getRecipeId());
        assertEquals(found.get(1).getRecipeName(), testRecipe1.getRecipeName());
    }
}
