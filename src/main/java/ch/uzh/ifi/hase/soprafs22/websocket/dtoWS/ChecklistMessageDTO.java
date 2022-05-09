package ch.uzh.ifi.hase.soprafs22.websocket.dtoWS;

public class ChecklistMessageDTO {

    private Long ingredientId;
    private Long takerId;

    public Long getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public Long getTakerId() {
        return takerId;
    }

    public void setTakerId(Long takerId) {
        this.takerId = takerId;
    }


}
