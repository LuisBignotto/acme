package br.com.acmeairlines.domain.users.repository;

import br.com.acmeairlines.domain.users.model.Role;
import br.com.acmeairlines.domain.users.model.UserModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        UserModel user1 = new UserModel("1", "Test User 1", "test1@example.com", "password", null, true, null, Role.REGULAR_USER);
        UserModel user2 = new UserModel("2", "Test User 2", "test2@example.com", "password", null, false, null, Role.REGULAR_USER);
        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    void findByActive_WhenActive_ReturnsActiveUsers() {
        Page<UserModel> activeUsers = userRepository.findByActive(true, PageRequest.of(0, 10));

        assertFalse(activeUsers.isEmpty());
        assertTrue(activeUsers.getContent().stream().allMatch(UserModel::getActive));
    }

    @Test
    void findByActive_WhenInactive_ReturnsInactiveUsers() {
        Page<UserModel> inactiveUsers = userRepository.findByActive(false, PageRequest.of(0, 10));

        assertFalse(inactiveUsers.isEmpty());
        assertTrue(inactiveUsers.getContent().stream().noneMatch(UserModel::getActive));
    }

    @Test
    void findByEmail_ExistingEmail_ReturnsUser() {
        UserModel foundUser = userRepository.findByEmail("test1@example.com");

        assertNotNull(foundUser);
        assertEquals("Test User 1", foundUser.getName());
        assertEquals(Role.REGULAR_USER, foundUser.getRole());
    }

    @Test
    void findByEmail_NonExistingEmail_ReturnsNull() {
        UserModel foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }
}
