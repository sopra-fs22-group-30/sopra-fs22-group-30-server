package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.IngredientRepository;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.RecipeRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class PartyServiceTest {
    @Mock
    private PartyRepository partyRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @InjectMocks
    private PartyService partyService;

    private Party testParty;
    private Recipe testRecipe;
    private User testUser;

    @InjectMocks
    private RecipeService recipeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        //given
        testParty = new Party();
        testParty.setPartyId(1L);
        testParty.setPartyName("testPartyName");
        List<String> partyAttendantsList = new ArrayList<>();
        partyAttendantsList.add("testUsername");
        testParty.setPartyAttendantsList(partyAttendantsList);
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("eggs");
        ingredient.setAmount(50);
        ingredients.add(ingredient);
        testParty.setIngredients(ingredients);
        testParty.setPartyIntro("Have fun!");
        testParty.setPlace("Zurich");
        testParty.setPartyHostId(1L);

        testRecipe = new Recipe();
        testRecipe.setRecipeId(1L);
        testRecipe.setRecipeName("testRecipeName");
        testRecipe.setIngredients(ingredients);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        Set<Long> partyName = new HashSet<>();
        partyName.add(1L);
        testUser.setJoinParties(partyName);
        Set<Party> partySet = new HashSet<>();
        partySet.add(testParty);
        testUser.setHostParties(partySet);

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(testRecipe);
        Mockito.when(partyRepository.save(Mockito.any())).thenReturn(testParty);
        Mockito.when(userRepository.save(Mockito.any())).thenReturn(testUser);
    }

    @Test
    public void createParty_validInputs_success() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(recipeRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testRecipe));

        Party createdParty = partyService.createParty(testParty);

        Mockito.verify(partyRepository, Mockito.times(1)).save(Mockito.any());

        assertEquals(testParty.getPartyId(), createdParty.getPartyId());
        assertEquals(testParty.getPartyName(), createdParty.getPartyName());
    }

    @Test
    public void getPartyById_success() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testParty));

        Party foundParty = partyService.getPartyById(1L, 1L);

        assertEquals(testParty.getPartyId(), foundParty.getPartyId());
    }

    @Test
    public void test_getPartiesAUserIsIn() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testParty));

        List<Party> testPartyList;
        testPartyList = partyService.getPartiesAUserIsIn(1L);

        assertEquals(testPartyList.get(0), testParty);
    }

    @Test
    public void test_editParty() {
        Mockito.when(userRepository.findByUsername(Mockito.any())).thenReturn(testUser);
        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testParty));

        User newUser = new User();
        newUser.setUsername("newUser");
        newUser.setPassword("111");

        Party newParty = new Party();
        newParty.setPartyName("newPartyName");
        List<String> partyAttendantsList = new ArrayList<>();
        partyAttendantsList.add("testUsername");
        partyAttendantsList.add("newUser");
        newParty.setPartyAttendantsList(partyAttendantsList);
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("eggs");
        ingredient.setAmount(50);
        ingredients.add(ingredient);
        newParty.setIngredients(ingredients);
        newParty.setPartyIntro("Have fun!");
        newParty.setPlace("Zurich");
        newParty.setTime(new Date());

        partyService.editParty(1L, 1L, newParty);

        assertEquals(testParty.getPartyName(), newParty.getPartyName());
    }

    @Test
    public void deleteParty_success() {
        List<Ingredient> ingredients = new ArrayList<>();
        Ingredient ingredient = new Ingredient();
        ingredient.setName("eggs");
        ingredient.setAmount(50);
        ingredients.add(ingredient);

        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testParty));
        Mockito.when(ingredientRepository.findByPartyId(Mockito.any())).thenReturn(ingredients);

        partyService.deleteParty(1L, 1L);
        Mockito.verify(partyRepository).deleteById(1L);
    }

    @Test
    public void quitParty_success() {
        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testParty));

        List<String> partyAttendantsList = new ArrayList<>();
        partyAttendantsList.add("testUsername");
        partyAttendantsList.add("newUser");
        testParty.setPartyAttendantsList(partyAttendantsList);
        testParty.setPartyAttendantsNum(2);

        User newUser = new User();
        newUser.setId(2L);
        newUser.setUsername("newUser");
        newUser.setPassword("111");
        Set<Long> partyName = new HashSet<>();
        partyName.add(1L);
        newUser.setJoinParties(partyName);

        Mockito.when(userRepository.save(Mockito.any())).thenReturn(newUser);
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.of(newUser));

        partyService.quitParty(2L, 1L);

        List<String> partyAttendantsList1 = new ArrayList<>();
        partyAttendantsList1.add("testUsername");
        assertEquals(testParty.getPartyAttendantsList(), partyAttendantsList1);
    }
}
