package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
        testParty.setIngredients(ingredients);
        testParty.setPartyIntro("Have fun!");
        testParty.setPlace("Zurich");

        testRecipe = new Recipe();
        testRecipe.setRecipeId(1L);
        testRecipe.setRecipeName("testRecipeName");
        testRecipe.setIngredients(ingredients);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        Set<String> partyName = new HashSet<>();
        partyName.add("testPartyName");
        testUser.setJoinParties(partyName);

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
    public void test_getPartyById_success() {
        Mockito.when(userRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testUser));
        Mockito.when(partyRepository.findById(Mockito.any())).thenReturn(java.util.Optional.ofNullable(testParty));

        Party foundParty = partyService.getPartyById(1L, 1L);

        assertEquals(testParty.getPartyId(), foundParty.getPartyId());
    }
}
