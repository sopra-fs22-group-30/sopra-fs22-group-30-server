package ch.uzh.ifi.hase.soprafs22.entity;

import ch.uzh.ifi.hase.soprafs22.constant.Gender;
import ch.uzh.ifi.hase.soprafs22.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Internal User Representation
 * This class composes the internal representation of the user and defines how
 * the user is stored in the database.
 * Every variable will be mapped into a database field with the @Column
 * annotation
 * - nullable = false -> this cannot be left empty
 * - unique = true -> this value must be unqiue across the database -> composes
 * the primary key
 */
@Entity
@Table(name = "USER")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String token;

    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    @Column(nullable = true)
    private Date birthday;

    @JsonFormat(pattern = "dd.MM.yyyy", locale = "de_CH")
    @Column(nullable = true)
    private Date creationDate;

    @Column(nullable = true)
    private String intro;

    @Column(nullable = true)
    private Gender gender;

    @OneToMany(mappedBy = "authorId", cascade = CascadeType.MERGE)
    private Set<Recipe> recipes;

    @OneToMany(mappedBy = "partyHostId")
    private Set<Party> hostParties;

    @ElementCollection
    private Set<String> joinParties;


    @ManyToMany
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender){
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

    public Set<String> getJoinParties() {
        return joinParties;
    }

    public void setJoinParties(Set<String> joinParties) {
        this.joinParties = joinParties;
    }


    public void addJoinParties(String partyName) {
        this.joinParties.add(partyName);

    }

    public List<Recipe> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Recipe> likeList) {
        this.likeList = likeList;
    }
}
