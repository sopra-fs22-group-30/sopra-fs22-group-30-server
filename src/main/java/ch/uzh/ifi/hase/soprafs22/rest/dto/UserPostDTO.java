package ch.uzh.ifi.hase.soprafs22.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class UserPostDTO {

    private String username;

    private String password;

    @JsonFormat(pattern="yyyy-MM-dd", locale = "de_CH")
    private Date createDate;

    @JsonFormat(pattern="yyyy-MM-dd", locale = "de_CH")
    private Date birthday;

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
}
