package ch.uzh.ifi.hase.soprafs22.websocket.dtoWS;

import java.util.List;

public class InvitationNotificationDTO {
    private Long partyId;
    private String partyName;
    private String hostName;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

}
