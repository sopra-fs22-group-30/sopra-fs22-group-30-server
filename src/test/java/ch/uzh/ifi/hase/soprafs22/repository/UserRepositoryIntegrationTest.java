package ch.uzh.ifi.hase.soprafs22.repository;

import ch.uzh.ifi.hase.soprafs22.entity.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void finish() {
        entityManager.clear();
    }

    @Test
    public void findByName_success() {
        // given
        User user = new User();
        user.setUsername("TestName");
        user.setPassword("TestPassword");
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());
    }

    @Test
    public void findByID_success() {
        // given
        User user = new User();
        user.setUsername("Alan");
        user.setPassword("TestPassword");
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        Optional<User> checkUser = userRepository.findById(user.getId());
        User found = null;
        if (checkUser.isPresent()) {
            found = checkUser.get();
        }
        //then
        assert found != null;
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getToken(), user.getToken());


    }

    @Test
    public void findAll() {
        // given
        User user1 = new User();
        user1.setUsername("Alan");
        user1.setPassword("TestPassword");
        user1.setToken("1");

        User user2 = new User();
        user2.setUsername("Bob");
        user2.setPassword("TestPassword");
        user2.setToken("2");

        entityManager.persist(user1);
        entityManager.flush();
        entityManager.persist(user2);
        entityManager.flush();

        // when
        List<User> found = userRepository.findAll();

        // then
        assertNotNull(found.get(0).getId());
        assertEquals(found.get(0).getId(), user1.getId());
        assertEquals(found.get(0).getUsername(), user1.getUsername());
        assertEquals(found.get(0).getToken(), user1.getToken());

        assertNotNull(found.get(1).getId());
        assertEquals(found.get(1).getId(), user2.getId());
        assertEquals(found.get(1).getUsername(), user2.getUsername());
        assertEquals(found.get(1).getToken(), user2.getToken());
    }
}
