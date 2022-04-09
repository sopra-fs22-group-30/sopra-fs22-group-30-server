package ch.uzh.ifi.hase.soprafs22.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "INGREDIENT")
public class Ingredient implements Serializable {

    @Id
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



//    public Long getIngredientId() {
//        return ingredientId;
//    }
//
//    public void setIngredientId(Long ingredientId) {
//        this.ingredientId = ingredientId;
//    }
}
