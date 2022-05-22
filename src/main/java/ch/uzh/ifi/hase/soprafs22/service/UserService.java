package ch.uzh.ifi.hase.soprafs22.service;

import ch.uzh.ifi.hase.soprafs22.entity.Party;
import ch.uzh.ifi.hase.soprafs22.entity.User;
import ch.uzh.ifi.hase.soprafs22.repository.PartyRepository;
import ch.uzh.ifi.hase.soprafs22.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * User Service
 * This class is the "worker" and responsible for all functionality related to
 * the user
 * (e.g., it creates, modifies, deletes, finds). The result will be passed back
 * to the caller.
 */
@Service
@Transactional
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;

    private final PartyRepository partyRepository;

    @Autowired
    public UserService(@Qualifier("userRepository") UserRepository userRepository, @Qualifier("partyRepository") PartyRepository partyRepository) {
        this.userRepository = userRepository;
        this.partyRepository = partyRepository;
    }

    public List<User> getUsers() {
        return this.userRepository.findAll();
    }

    // register
    public User createUser(User newUser) {
        newUser.setToken(UUID.randomUUID().toString());
        checkIfUserExists(newUser);

        // saves the given entity but data is only persisted in the database once
        // flush() is called
        newUser.setCreationDate(new Date());
        newUser = userRepository.save(newUser);
        userRepository.flush();

        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }


    private void checkIfUserExists(User userToBeCreated) {
        User userByUsername = userRepository.findByUsername(userToBeCreated.getUsername());

        String baseErrorMessage = "The %s provided %s not unique. Therefore, the user could not be created!";
        if (userByUsername != null) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    String.format(baseErrorMessage, "username", "is"));
        }
    }

    // login
    public User loginUser(User user) {
        user = checkIfPasswordWrong(user);
        user.setToken(UUID.randomUUID().toString());

        return user;
    }

    private User checkIfPasswordWrong(User userToBeLoggedIn) {

        User userByUsername = userRepository.findByUsername(userToBeLoggedIn.getUsername());

        if (userByUsername == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Username may not exist!!");
        }
        else if (!userByUsername.getPassword().equals(userToBeLoggedIn.getPassword())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Password Incorrect!");
        }
        else {
            return userByUsername;
        }
    }

    public User getUserById(Long id) {
        Optional<User> checkUser = userRepository.findById(id);
        if (checkUser.isPresent()) {
            return checkUser.get();
        }
        else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User was not found!");
        }
    }

    // logout
    public void logoutUser(User user) {
        user.setToken("");
    }


    public void editUser(User userInput) {
        if(!userRepository.existsById(userInput.getId())) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user ID was not found");
        }
        User user = getUserById(userInput.getId());

        // edit username
        User userToBeUpdated = getUserById(userInput.getId());
        if (userInput.getUsername().equals(userToBeUpdated.getUsername())) {
            userToBeUpdated.setBirthday(userInput.getBirthday());
            user.setGender(userInput.getGender());
            user.setIntro(userInput.getIntro());
            user.setProfilePictureLocation(userInput.getProfilePictureLocation());
        } else if (userRepository.findByUsername(userInput.getUsername()) == null) {
            for (Party party : partyRepository.findAll()) {
                if (party.getPartyAttendantsList().contains(user.getUsername())) {
                    party.getPartyAttendantsList().remove(user.getUsername());
                    party.getPartyAttendantsList().add(userInput.getUsername());
                    partyRepository.saveAndFlush(party);
                }
            }
            userToBeUpdated.setUsername(userInput.getUsername());
            userToBeUpdated.setBirthday(userInput.getBirthday());
            user.setGender(userInput.getGender());
            user.setIntro(userInput.getIntro());
            user.setProfilePictureLocation(userInput.getProfilePictureLocation());

        } else {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "new username already exists");
        }


        userRepository.save(user);
        userRepository.flush();
    }
}



