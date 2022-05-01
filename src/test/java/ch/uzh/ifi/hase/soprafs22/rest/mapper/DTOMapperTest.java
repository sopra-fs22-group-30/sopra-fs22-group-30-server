package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.ingredient.IngredientGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.ingredient.IngredientPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserPutDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * DTOMapperTest
 * Tests if the mapping between the internal and the external/API representation
 * works.
 */
public class DTOMapperTest {
    @Test
    public void testCreateUser_fromUserPostDTO_toUser_success() {
        // create UserPostDTO
        UserPostDTO userPostDTO = new UserPostDTO();
        userPostDTO.setUsername("username");

        // MAP -> Create user
        User user = DTOMapper.INSTANCE.convertUserPostDTOtoEntity(userPostDTO);

        // check content
        assertEquals(userPostDTO.getUsername(), user.getUsername());
    }

    @Test
    public void testGetUser_fromUser_toUserGetDTO_success() {
        // create User
        User user = new User();
        user.setUsername("firstname@lastname");
        user.setToken("1");

        // MAP -> Create UserGetDTO
        UserGetDTO userGetDTO = DTOMapper.INSTANCE.convertEntityToUserGetDTO(user);

        // check content
        assertEquals(user.getId(), userGetDTO.getId());
        assertEquals(user.getUsername(), userGetDTO.getUsername());
    }

    @Test
    public void testConvert_fromPutDTO_toUser(){
        UserPutDTO userPutDTO = new UserPutDTO();

        userPutDTO.setId(1L);
        userPutDTO.setUsername("un");
        userPutDTO.setToken("1");

        User user = DTOMapper.INSTANCE.convertUserPutDTOtoEntity(userPutDTO);

        assertEquals(user.getId(), userPutDTO.getId());
        assertEquals(user.getUsername(), userPutDTO.getUsername());
    }

    @Test
    public void test_ConvertEntityToIngredientsGetDTO(){
        // create Ingredients
        Ingredient ingredient = new Ingredient();
        ingredient.setName("beef");

        // MAP -> Create UserGetDTO
        IngredientGetDTO ingredientGetDTO = DTOMapper.INSTANCE.convertEntityToIngredientGetDTO(ingredient);

        // check content
        assertEquals(ingredient.getIngredientId(), ingredientGetDTO.getIngredientId());
        assertEquals(ingredient.getName(), ingredientGetDTO.getName());
    }

    @Test
    public void test_convertIngredientPostDTOtoEntity(){
        // create IngredientPostDTO
        IngredientPostDTO ingredientPostDTO = new IngredientPostDTO();
        ingredientPostDTO.setIngredientId(1L);
        ingredientPostDTO.setName("beef");
        ingredientPostDTO.setAmount(50);

        // MAP -> Create ingredient
        Ingredient ingredient = DTOMapper.INSTANCE.convertIngredientPostDTOtoEntity(ingredientPostDTO);

        // check content
        assertEquals(ingredientPostDTO.getIngredientId(), ingredient.getIngredientId());
        assertEquals(ingredientPostDTO.getName(), ingredient.getName());
        assertEquals(ingredientPostDTO.getAmount(), ingredient.getAmount());
    }
}


