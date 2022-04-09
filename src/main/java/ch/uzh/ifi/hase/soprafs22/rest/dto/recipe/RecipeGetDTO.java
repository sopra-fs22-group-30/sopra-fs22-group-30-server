package ch.uzh.ifi.hase.soprafs22.rest.dto.recipe;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.entity.Ingredient;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecipeGetDTO{

    private Long recipeId;
    private String recipeName;
    private Long authorId;
    private Cuisine cuisine;
    private Long cost;
    private Long timeConsumed;
    private String pictureLocation;
    private List<Ingredient> ingredients;
    private Integer portion;
    private String content;
    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    private Date creationDate;
    private Long likesNum;

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeName() {
        return recipeName;
    }

    public void setRecipeName(String recipeName) {
        this.recipeName = recipeName;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public Cuisine getCuisine() {
        return cuisine;
    }

    public void setCuisine(Cuisine cuisine) {
        this.cuisine = cuisine;
    }

    public Long getCost() {
        return cost;
    }

    public void setCost(Long cost) {
        this.cost = cost;
    }


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getTimeConsumed() {
        return timeConsumed;
    }

    public void setTimeConsumed(Long timeConsumed) {
        this.timeConsumed = timeConsumed;
    }

    public String getPictureLocation() {
        return pictureLocation;
    }

    public void setPictureLocation(String pictureLocation) {
        this.pictureLocation = pictureLocation;
    }

    public Long getLikesNum() {
        return likesNum;
    }

    public void setLikesNum(Long likesNum) {
        this.likesNum = likesNum;
    }

    public Integer getPortion() {
        return portion;
    }

    public void setPortion(Integer portion) {
        this.portion = portion;
    }


    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
