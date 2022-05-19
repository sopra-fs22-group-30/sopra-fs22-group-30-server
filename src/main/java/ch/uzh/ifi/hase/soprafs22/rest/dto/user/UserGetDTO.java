package ch.uzh.ifi.hase.soprafs22.rest.dto.user;

import ch.uzh.ifi.hase.soprafs22.constant.Gender;
import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.Recipe;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserGetDTO {

    private Long id;
    private String username;
    private String token;
    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    private Date birthday;
    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    private Date creationDate;
    private String intro;
    private Gender gender;
    private Set<Recipe> recipes;

    private String profilePictureLocation;

    private Set<Party> hostParties;

    private Set<Long> joinParties;

    private List<Recipe> likeList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getProfilePictureLocation(){
        return profilePictureLocation;
    }

    public void setProfilePictureLocation(String profilePictureLocation){
        this.profilePictureLocation=profilePictureLocation;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Set<Party> getHostParties() {
        return hostParties;
    }

    public void setHostParties(Set<Party> hostParties) {
        this.hostParties = hostParties;
    }


    public Set<Long> getJoinParties() {
        return joinParties;
    }

    public void setJoinParties(HashSet<Long> joinParties) {
        this.joinParties = joinParties;
    }

    public List<Recipe> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Recipe> likeList) {
        this.likeList = likeList;
    }
}
