package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.constant.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Duration;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "RECIPE")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private Long recipeId;

    @Column(nullable = false, unique = false)
    private String recipeName;

    @Column(nullable = false, unique = false)
    private Long authorId;

    @Column(nullable = false, unique = false)
    private Cuisine cuisine;

    @Column(nullable = false, unique = false)
    private Long cost;

    @Column(nullable = false, unique = false)
    private String ingredient;


    @Column
    private Long timeConsumed;

    @Column
    private Integer portion;

    @Column
    private  String pictureLocation;

    @Column(nullable = false, unique = false)
    private String content;

    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    @Column(nullable = true)
    private Date creationDate;

    @Column(nullable = false)
    private Long likeNum;




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

    public String getIngredient() {
        return ingredient;
    }

    public void setIngredient(String ingredient) {
        this.ingredient = ingredient;
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

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
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

    public Long getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Long likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getPortion() {
        return portion;
    }

    public void setPortion(Integer portion) {
        this.portion = portion;
    }
}
