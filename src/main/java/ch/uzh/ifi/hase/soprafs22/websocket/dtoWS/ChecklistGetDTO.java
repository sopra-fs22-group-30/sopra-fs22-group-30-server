package ch.uzh.ifi.hase.soprafs22.websocket.dtoWS;

public class ChecklistGetDTO {

	private Long partyId;
    private Long ingredientId;
    private String ingredientName;
    private String taker;


	public Long getPartyId() {
		return partyId;
	}
    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public Long getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(Long ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public String getTaker() {
        return taker;
    }
    public void setTaker(String taker) {
        this.taker = taker;
    }
}
