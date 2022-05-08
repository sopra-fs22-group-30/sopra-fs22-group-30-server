package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
public class PartyRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PartyRepository partyRepository;

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private UserRepository userRepository;

    User testUser;
    Recipe testRecipe;
    Party testParty;
    Party testParty1;

    @BeforeEach
    public void setup() {

        //given
        testUser = new User();
        testUser.setUsername("testUsername");
        testUser.setPassword("testPassword");
        testUser.setToken("1");
        Set<Long> partyName = new HashSet<>();
        partyName.add(1L);
        partyName.add(2L);
        testUser.setJoinParties(partyName);

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

        testParty = new Party();
        testParty.setPartyName("testPartyName");
        List<String> partyAttendantsList = new ArrayList<>();
        partyAttendantsList.add("testUsername");
        testParty.setPartyAttendantsList(partyAttendantsList);
        testParty.setPartyIntro("Have fun!");
        testParty.setPlace("Zurich");
        testParty.setPartyHostId(testUser.getId());
        testParty.setRecipeUsedId(testRecipe.getRecipeId());
        testParty.setTime(new Date());

        entityManager.persist(testParty);
        entityManager.flush();

        testParty1 = new Party();
        testParty1.setPartyName("testPartyName1");
        List<String> partyAttendantsList1 = new ArrayList<>();
        partyAttendantsList1.add("testUsername");
        testParty.setPartyAttendantsList(partyAttendantsList1);
        List<Ingredient> ingredients1 = new ArrayList<>();
        testParty1.setIngredients(ingredients1);
        testParty1.setPartyIntro("Have fun too!");
        testParty1.setPlace("Zurich");
        testParty1.setPartyHostId(testUser.getId());
        testParty1.setRecipeUsedId(testRecipe.getRecipeId());
        testParty1.setTime(new Date());

        entityManager.persist(testParty1);
        entityManager.flush();
    }

    @Test
    public void findAll() {

        // when
        List<Party> found = partyRepository.findAll();

        // then
        assertNotNull(found.get(0).getPartyId());
        assertEquals(found.get(0).getPartyId(), testParty.getPartyId());
        assertEquals(found.get(0).getPartyName(), testParty.getPartyName());

        assertNotNull(found.get(1).getPartyId());
        assertEquals(found.get(1).getPartyId(), testParty1.getPartyId());
        assertEquals(found.get(1).getPartyName(), testParty1.getPartyName());

    }

    @Test
    public void findByID_success() {

        // when
        Optional<Party> checkParty = partyRepository.findById(testParty.getPartyId());
        Party found = null;
        if (checkParty.isPresent()) {
            found = checkParty.get();
        }
        // then
        assert found != null;
        assertNotNull(found.getPartyId());
        assertEquals(found.getPartyName(), testParty.getPartyName());
    }
}
