package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
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

public class PartyServiceTest {
    @Mock
    private PartyRepository partyRepository;

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private PartyService partyService;

    private Party testParty;

    @InjectMocks
    private RecipeService recipeService;

    private Recipe testRecipe;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        //given
        testParty = new Party();
        testParty.setPartyId(1L);
        testParty.setPartyName("testPartyName");
        List<String> partyAttendantsList = new ArrayList<>();
        testParty.setPartyAttendantsList(partyAttendantsList);
        List<Ingredient> ingredients = new ArrayList<>();
        testParty.setIngredients(ingredients);

        testRecipe = new Recipe();
        testRecipe.setRecipeId(1L);
        testRecipe.setRecipeName("testRecipeName");
        testRecipe.setIngredients(ingredients);

        // when -> any object is being saved in the userRepository -> return the dummy
        // testUser
        Mockito.when(recipeRepository.save(Mockito.any())).thenReturn(testRecipe);
        Mockito.when(partyRepository.save(Mockito.any())).thenReturn(testParty);
    }

//    @Test
//    public void createParty_validInputs_success() {
//        Recipe createdRecipe = recipeService.createRecipe(testRecipe);
//        Mockito.verify(recipeRepository, Mockito.times(1)).save(Mockito.any());
//
//        Party createdParty = partyService.createParty(testParty);
//
//        Mockito.verify(partyRepository, Mockito.times(1)).save(Mockito.any());
//
//        assertEquals(testParty.getPartyId(), createdParty.getPartyId());
//        assertEquals(testParty.getPartyName(), createdParty.getPartyName());
//    }
}
