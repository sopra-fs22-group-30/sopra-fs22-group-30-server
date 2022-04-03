package ch.uzh.ifi.hase.soprafs22.rest.mapper;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserPostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.user.UserPutDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

/**
 * DTOMapper
 * This class is responsible for generating classes that will automatically
 * transform/map the internal representation
 * of an entity (e.g., the User) to the external/API representation (e.g.,
 * UserGetDTO for getting, UserPostDTO for creating)
 * and vice versa.
 * Additional mappers can be defined for new entities.
 * Always created one mapper for getting information (GET) and one mapper for
 * creating information (POST).
 */
@Mapper
public interface DTOMapper {

  DTOMapper INSTANCE = Mappers.getMapper(DTOMapper.class);

  @Mapping(source = "username", target = "username")
  @Mapping(source = "password", target = "password")
  User convertUserPostDTOtoEntity(UserPostDTO userPostDTO);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "token", target = "token")
  @Mapping(source = "creationDate", target = "creationDate")
  @Mapping(source = "intro", target = "intro")
  @Mapping(source = "gender", target = "gender")
  UserGetDTO convertEntityToUserGetDTO(User user);

  @Mapping(source = "id", target = "id")
  @Mapping(source = "username", target = "username")
  @Mapping(source = "birthday", target = "birthday")
  @Mapping(target = "token", ignore = true)
  @Mapping(target = "password", ignore = true)
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(source = "intro", target = "intro")
  @Mapping(source = "gender", target = "gender")
  User convertUserPutDTOtoEntity(UserPutDTO userPutDTO);

  @Mapping(source = "recipeId", target = "recipeId")
  @Mapping(source = "recipeName", target = "recipeName")
  @Mapping(source = "authorId", target = "authorId")
  @Mapping(source = "cuisine", target = "cuisine")
  @Mapping(source = "cost", target = "cost")
  @Mapping(source = "ingredient", target = "ingredient")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "creationDate", target = "creationDate")
  Recipe convertRecipePostDTOtoEntity(RecipePostDTO RecipePostDTO);

  @Mapping(source = "recipeId", target = "recipeId")
  @Mapping(source = "recipeName", target = "recipeName")
  @Mapping(source = "authorId", target = "authorId")
  @Mapping(source = "cuisine", target = "cuisine")
  @Mapping(source = "cost", target = "cost")
  @Mapping(source = "ingredient", target = "ingredient")
  @Mapping(source = "content", target = "content")
  @Mapping(source = "creationDate", target = "creationDate")
  RecipeGetDTO convertEntityToRecipeGetDTO(Recipe recipe);
}
