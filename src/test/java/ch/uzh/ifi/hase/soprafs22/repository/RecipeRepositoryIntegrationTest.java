//package ch.uzh.ifi.hase.soprafs22.repository;
//
//import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
//import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//
//@DataJpaTest
//public class RecipeRepositoryIntegrationTest {
//
//    @Autowired
//    private TestEntityManager entityManager;
//
//    @Autowired
//    private RecipeRepository recipeRepository;
//
//    @Test
//    public void findAll() {
//        // given
//        Recipe recipe1 = new Recipe();
////        recipe1.setRecipeId(10L);
//        recipe1.setRecipeName("r1");
//        recipe1.setAuthorId(10L);
//        recipe1.setCuisine(Cuisine.Algerian);
//        recipe1.setCost(30L);
//        recipe1.setContent("content");
//        recipe1.setLikesNum(10L);
//
//        Recipe recipe2 = new Recipe();
////        recipe2.setRecipeId(20L);
//        recipe2.setRecipeName("r2");
//        recipe2.setAuthorId(11L);
//        recipe2.setCuisine(Cuisine.Chinese);
//        recipe2.setCost(31L);
//        recipe2.setContent("content1");
//        recipe2.setLikesNum(11L);
//
//        entityManager.persist(recipe1);
//        entityManager.flush();
//        entityManager.persist(recipe2);
//        entityManager.flush();
//
//        // when
//        List<Recipe> found = recipeRepository.findAll();
//
//        // then
//        assertNotNull(found.get(0).getRecipeId());
//        assertEquals(found.get(0).getRecipeId(), recipe1.getRecipeId());
//        assertEquals(found.get(0).getRecipeName(), recipe1.getRecipeName());
//
//        assertNotNull(found.get(1).getRecipeId());
//        assertEquals(found.get(1).getRecipeId(), recipe2.getRecipeId());
//        assertEquals(found.get(1).getRecipeName(), recipe2.getRecipeName());
//    }
//
//    @Test
//    public void findByID_success() {
//        // given
//        Recipe recipe = new Recipe();
//        recipe.setRecipeName("r1");
//        recipe.setAuthorId(1L);
//        recipe.setCuisine(Cuisine.Algerian);
//        recipe.setCost(30L);
//        recipe.setContent("content");
//        recipe.setLikesNum(0L);
//
//        entityManager.persist(recipe);
//        entityManager.flush();
//
//        // when
//        Optional<Recipe> checkRecipe = recipeRepository.findById(recipe.getRecipeId());
//        Recipe found = null;
//        if (checkRecipe.isPresent()) {
//            found = checkRecipe.get();
//        }
//        // then
//        assert found != null;
//        assertNotNull(found.getRecipeId());
//        assertEquals(found.getRecipeName(), recipe.getRecipeName());
//        assertEquals(found.getRecipeId(), recipe.getRecipeId());
//    }
//}
