package ch.uzh.ifi.hase.soprafs22.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INGREDIENT")
public class Ingredient implements Serializable {

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="seq")
    private Long ingredientId;

    @Column
    private Long recipeId;

    private String name;
    private Integer amount;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Long recipeId) {
        this.recipeId = recipeId;
    }


//    public Long getIngredientId() {
//        return ingredientId;
//    }
//
//    public void setIngredientId(Long ingredientId) {
//        this.ingredientId = ingredientId;
//    }
}
