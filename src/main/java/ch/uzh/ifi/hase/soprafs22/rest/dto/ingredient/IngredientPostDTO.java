package ch.uzh.ifi.hase.soprafs22.rest.dto.ingredient;

import ch.uzh.ifi.hase.soprafs22.entity.Recipe;

public class IngredientPostDTO {
    private Long ingredientId;
    private Long recipeId;
    private String name;
    private Integer amount;
    private Long partyId;
    private Long takerId;
    private String takerName;

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

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getTakerId() {
        return takerId;
    }

    public void setTakerId(Long takerId) {
        this.ingredientId = takerId;
    }

    public String getTakerName() {
        return takerName;
    }

    public void setTakerName(String takerName) {
        this.takerName = takerName;
    }
}
