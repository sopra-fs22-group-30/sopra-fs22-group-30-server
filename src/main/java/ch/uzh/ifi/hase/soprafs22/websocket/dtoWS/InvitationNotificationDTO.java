package ch.uzh.ifi.hase.soprafs22.websocket.dtoWS;

import java.util.List;

public class InvitationNotificationDTO {
    private Long hostId;
    private Long partyId;
    private List<Long> attendantsList;

    public Long getHostId() {
        return hostId;
    }

    public void setHostId(Long hostId) {
        this.hostId = hostId;
    }

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public List<Long> getAttendantsList() {
        return attendantsList;
    }

    public void setAttendantsList(List<Long> attendantsList) {
        this.attendantsList = attendantsList;
    }
}
