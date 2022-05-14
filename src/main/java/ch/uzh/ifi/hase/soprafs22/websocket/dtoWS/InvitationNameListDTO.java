package ch.uzh.ifi.hase.soprafs22.websocket.dtoWS;

import java.util.List;

public class InvitationNameListDTO {
    private Long partyId;
    private List<String> partyAttendantsList;


    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public List<String> getPartyAttendantsList() {
        return partyAttendantsList;
    }

    public void setPartyAttendantsList(List<String> partyAttendantsList) {
        this.partyAttendantsList = partyAttendantsList;
    }
}