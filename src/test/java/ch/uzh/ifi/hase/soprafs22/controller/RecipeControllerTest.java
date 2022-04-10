package ch.uzh.ifi.hase.soprafs22.controller;

import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipeGetDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePostDTO;
import ch.uzh.ifi.hase.soprafs22.rest.dto.recipe.RecipePutDTO;
import ch.uzh.ifi.hase.soprafs22.rest.mapper.DTOMapper;
import ch.uzh.ifi.hase.soprafs22.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Test
    void getAllRecipes() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testRecipeName");
        recipe.setAuthorId(1L);
        recipe.setContent("TestContent");

        List<Recipe> allRecipe = Collections.singletonList(recipe);

        given(recipeService.getRecipes()).willReturn(allRecipe);

        MockHttpServletRequestBuilder getRequest = get("/recipes").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(getRequest).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].recipeId", is(recipe.getRecipeId().intValue())))
                .andExpect(jsonPath("$[0].recipeName", is(recipe.getRecipeName())))
                .andExpect(jsonPath("$[0].authorId", is(recipe.getAuthorId().intValue())))
                .andExpect(jsonPath("$[0].content", is(recipe.getContent())));
    }

    //get recipe with a valid input
    @Test
    void recipeGetDTO_validInput() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("Test Recipe");
        recipe.setAuthorId(2L);
        recipe.setContent("Test Content");

        //should return
        RecipeGetDTO recipeGetDTO = new RecipeGetDTO();
        recipeGetDTO.setRecipeName(recipe.getRecipeName());
        recipeGetDTO.setAuthorId(recipe.getAuthorId());
        recipeGetDTO.setContent(recipe.getContent());

        // when/then -> do the request + validate the result
        Mockito.when(recipeService.getRecipeById(recipe.getRecipeId()))
                .thenReturn(recipe);

        MockHttpServletRequestBuilder getRequest = get("/recipes/1")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipeName", is(recipeGetDTO.getRecipeName())))
                .andExpect(jsonPath("$.authorId", is(recipeGetDTO.getAuthorId().intValue())))
                .andExpect(jsonPath("$.content", is(recipeGetDTO.getContent())));
    }

    //get recipe with a NOT valid input
    @Test
    void recipeGetDTO_invalidInput() throws Exception {
        // given
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("Test Recipe");
        recipe.setAuthorId(2L);
        recipe.setContent("Test Content");


        // when/then -> do the request + validate the result
        given(recipeService.getRecipeById(2L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));


        MockHttpServletRequestBuilder getRequest = get("/users/2")
                .contentType(MediaType.APPLICATION_JSON);

        // then
        mockMvc.perform(getRequest)
                .andExpect(status().isNotFound());
    }

    //create recipe with valid input
    @Test
    void createRecipe() throws Exception{
        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setAuthorId(3L);
        recipe.setContent("testContent");

        RecipePostDTO recipePostDTO = new RecipePostDTO();
        recipePostDTO.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipePostDTO.setAuthorId(3L);

        given(recipeService.createRecipe(Mockito.any())).willReturn(recipe);

        MockHttpServletRequestBuilder postRequest = post("/recipes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePostDTO));


        mockMvc.perform(postRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.recipeId", is(recipe.getRecipeId().intValue())))
                .andExpect(jsonPath("$.recipeName", is(recipe.getRecipeName())))
                .andExpect(jsonPath("$.authorId", is(recipe.getAuthorId().intValue())))
                .andExpect(jsonPath("$.content", is(recipe.getContent())));
    }



    //delete recipe with valid input
    @Test
    void deleteRecipe_valid_input() throws Exception{
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipe.setAuthorId(2L);

        doNothing().when(recipeService).deleteRecipe(2L,1L);


        MockHttpServletRequestBuilder deleteRequest = delete("/users/" + user.getId()+"/recipes/"+recipe.getRecipeId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNoContent());
    }

    //delete recipe with invalid input Forbidden
    @Test
    void deleteRecipe_invalid_input() throws Exception{
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipe.setAuthorId(2L);

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(recipeService).deleteRecipe(1L,1L);

        MockHttpServletRequestBuilder deleteRequest = delete("/users/" + user.getId()+"/recipes/"+recipe.getRecipeId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isForbidden());
    }

    //delete recipe with invalid input NOT found
    @Test
    void deleteRecipe_invalid_input2() throws Exception{
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipe.setAuthorId(2L);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(recipeService).deleteRecipe(1L,1L);

        MockHttpServletRequestBuilder deleteRequest = delete("/users/" + user.getId()+"/recipes/"+recipe.getRecipeId())
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(deleteRequest)
                .andExpect(status().isNotFound());
    }

    //edit recipe with valid input
    @Test
    void editRecipe_valid_input() throws Exception{
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipe.setAuthorId(1L);

        RecipePutDTO recipePutDTO = new RecipePutDTO();
        recipePutDTO.setContent("updated_new_content");

        Recipe updatedRecipe = DTOMapper.INSTANCE.convertRecipePutDTOtoEntity(recipePutDTO);

        doNothing().when(recipeService).editRecipe(1L,1L,updatedRecipe);


        MockHttpServletRequestBuilder putRequest = put("/users/" + user.getId()+"/recipes/"+recipe.getRecipeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNoContent());
    }

    //edit recipe with invalid input Forbidden(user not the author)
    @Test
    void editRecipe_invalid_input() throws Exception{
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipe.setAuthorId(2L);

        RecipePutDTO recipePutDTO = new RecipePutDTO();
        recipePutDTO.setContent("updated_new_content");

        Recipe updatedRecipe = DTOMapper.INSTANCE.convertRecipePutDTOtoEntity(recipePutDTO);

        doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN)).when(recipeService).editRecipe(Mockito.any(),Mockito.any(),Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/users/" + user.getId()+"/recipes/"+recipe.getRecipeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isForbidden());
    }

    //edit recipe with invalid input NOT Found(recipe not found)
    @Test
    void editRecipe_invalid_input2() throws Exception{
        User user = new User();
        user.setId(1L);

        Recipe recipe = new Recipe();
        recipe.setRecipeId(1L);
        recipe.setRecipeName("testUsername");
        recipe.setContent("testContent");
        recipe.setAuthorId(2L);

        RecipePutDTO recipePutDTO = new RecipePutDTO();
        recipePutDTO.setContent("updated_new_content");

        Recipe updatedRecipe = DTOMapper.INSTANCE.convertRecipePutDTOtoEntity(recipePutDTO);

        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND))
                .when(recipeService)
                .editRecipe(Mockito.any(),Mockito.any(),Mockito.any());

        MockHttpServletRequestBuilder putRequest = put("/users/" + user.getId()+"/recipes/"+recipe.getRecipeId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(recipePutDTO));

        mockMvc.perform(putRequest)
                .andExpect(status().isNotFound());
    }

    private String asJsonString(final Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch (JsonProcessingException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("The request body could not be created.%s", e.toString()));
        }
    }
}