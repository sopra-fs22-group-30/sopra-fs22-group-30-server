package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Cuisine;
import ch.uzh.ifi.hase.soprafs22.constant.Gender;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "RECIPE")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
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

    @Column(nullable = false, unique = false)
    private String content;

    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    @Column(nullable = true)
    private Date creationDate;

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
}
