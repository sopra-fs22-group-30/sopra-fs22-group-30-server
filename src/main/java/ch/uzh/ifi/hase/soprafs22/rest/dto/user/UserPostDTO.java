package ch.uzh.ifi.hase.soprafs22.rest.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserPostDTO {

    private String username;

    private String password;

    @JsonFormat(pattern="dd.MM.yyyy", locale = "de_CH")
    private Date createDate;

    @JsonFormat(pattern="dd.MM.yyyy", locale = "de_CH")
    private Date birthday;

    private String profilePictureLocation;

    public UserPostDTO() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getProfilePictureLocation(){return profilePictureLocation;}

    public void setProfilePictureLocation(String profilePictureLocation){this.profilePictureLocation=profilePictureLocation;}
}
