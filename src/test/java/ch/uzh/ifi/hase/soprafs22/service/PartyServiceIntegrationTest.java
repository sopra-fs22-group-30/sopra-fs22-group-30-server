package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
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
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebAppConfiguration
@SpringBootTest
public class PartyServiceIntegrationTest {
    @Qualifier("partyRepository")
    @Autowired
    private PartyRepository partyRepository;

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
    private PartyService partyService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    Party testParty;
    Party createdParty;
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

        testParty = new Party();
        testParty.setPartyName("testPartyName");
        List<String> partyAttendantsList = new ArrayList<>();
        partyAttendantsList.add("testName");
        testParty.setPartyAttendantsList(partyAttendantsList);
        testParty.setRecipeUsedId(createdRecipe.getRecipeId());
        testParty.setPartyIntro("Have fun!");
        testParty.setPlace("Zurich");
        testParty.setPartyHostId(createdUser.getId());

        createdParty = partyService.createParty(testParty);
    }

    @AfterEach
    public void finish() {
        recipeService.likeAndUnlike(testUser.getId(),testRecipe.getRecipeId());

        ingredientRepository.deleteAll();
        partyRepository.deleteAll();
        recipeRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void creatParty_success() {
        assertEquals(testParty.getPartyName(), createdParty.getPartyName());
    }

    @Test
    public void getPartyId_success() {
        Party foundParty = partyService.getPartyById(createdUser.getId(), createdParty.getPartyId());

        assertEquals(foundParty.getPartyId(), createdParty.getPartyId());
        assertEquals(foundParty.getPartyName(), createdParty.getPartyName());
    }

    @Test
    public void getPartyId_fail() {
        assertThrows(ResponseStatusException.class, () -> partyService.getPartyById(1000L,1000L));
    }

    @Test
    public void test_editParty() {
        Party newParty = new Party();
        newParty.setPartyName("testPartyName");
        List<String> partyAttendantsList = new ArrayList<>();
        partyAttendantsList.add("testName");
        newParty.setPartyAttendantsList(partyAttendantsList);
        newParty.setRecipeUsedId(createdRecipe.getRecipeId());
        newParty.setPartyIntro("New Have fun!");
        newParty.setPlace("Zurich Oerlikon");

        Party editedParty = partyService.editParty(createdUser.getId(), createdParty.getPartyId(), newParty);

        assertEquals(newParty.getPartyIntro(), editedParty.getPartyIntro());
        assertEquals(newParty.getPlace(), editedParty.getPlace());
    }

    @Test
    public void deleteParty_success() {
        Long id = createdParty.getPartyId();
        partyService.deleteParty(createdUser.getId(), createdParty.getPartyId());

        assertThrows(ResponseStatusException.class, () -> partyService.getPartyById(createdUser.getId(), id));
    }
}
