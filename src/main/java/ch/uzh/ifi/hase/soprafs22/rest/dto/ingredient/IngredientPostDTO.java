package ch.uzh.ifi.hase.soprafs22.rest.dto.ingredient;

public class IngredientPostDTO {
//    private Long ingredientId;
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
}
