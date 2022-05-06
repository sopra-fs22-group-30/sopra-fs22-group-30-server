package ch.uzh.ifi.hase.soprafs22.rest.dto.party;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class PartyPutDTO {

    private String partyName;

    private String partyIntro;

    private String place;

    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    private Date time;

    private List<String> partyAttendantsList;


    public String getPartyIntro() {
        return partyIntro;
    }

    public void setPartyIntro(String partyIntro) {
        this.partyIntro = partyIntro;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }


    public List<String> getPartyAttendantsList() {
        return partyAttendantsList;
    }

    public void setPartyAttendantsList(List<String> partyAttendantsList) {
        this.partyAttendantsList = partyAttendantsList;
    }

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }
}
