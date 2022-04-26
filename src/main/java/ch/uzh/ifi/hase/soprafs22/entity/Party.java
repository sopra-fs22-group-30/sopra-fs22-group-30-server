package ch.uzh.ifi.hase.soprafs22.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "PARTY")
public class Party implements Serializable {
    @Id
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="seq")
    private Long partyId;

    @Column(nullable = false)
    private String partyName;


    @Column(nullable = false)
    private Long partyHostId;

    @Column
    private String partyIntro;

    @Column
    private String place;

    @Column
    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    private Date time;

    @Column
    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    private Date creationDate;

    @Column(nullable = false)
    private Long recipeUsedId;


    @Column
    @OneToMany(mappedBy = "partyId", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private List<Ingredient> ingredients;

    @Column
    @ElementCollection
    private List<String> partyAttendantsList;

    @Column
    private Integer partyAttendantsNum;

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

    public Long getPartyHostId() {
        return partyHostId;
    }

    public void setPartyHostId(Long partyHostId) {
        this.partyHostId = partyHostId;
    }

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

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }



    public Long getRecipeUsedId() {
        return recipeUsedId;
    }

    public void setRecipeUsedId(Long recipeUsedId) {
        this.recipeUsedId = recipeUsedId;
    }



    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getPartyAttendantsList() {
        return partyAttendantsList;
    }

    public void setPartyAttendantsList(List<String> partyAttendantsList) {
        this.partyAttendantsList = partyAttendantsList;
    }

    public Integer getPartyAttendantsNum() {
        return partyAttendantsNum;
    }

    public void setPartyAttendantsNum(Integer partyAttendantsNum) {
        this.partyAttendantsNum = partyAttendantsNum;
    }
}
