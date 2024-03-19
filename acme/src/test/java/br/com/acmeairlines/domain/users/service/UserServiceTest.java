package br.com.acmeairlines.domain.users.service;

import br.com.acmeairlines.domain.users.dto.UserDataDTO;
import br.com.acmeairlines.domain.users.dto.UserRegisterDTO;
import br.com.acmeairlines.domain.users.dto.UserUpdateDTO;
import br.com.acmeairlines.domain.users.model.Role;
import br.com.acmeairlines.domain.users.model.UserModel;
import br.com.acmeairlines.domain.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_EmailAlreadyExists_ThrowsException() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Test Name", "test@example.com", "password", Role.REGULAR_USER, true);
        when(userRepository.findByEmail(anyString())).thenReturn(new UserModel());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createUser(userRegisterDTO);
        });

        assertEquals("Email already in use.", exception.getMessage());
    }

    @Test
    void createUser_Successful_CreatesUser() {
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO("Test Name", "test@example.com", "password", Role.REGULAR_USER, true);
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(userRepository.save(any(UserModel.class))).thenAnswer(i -> i.getArguments()[0]);

        UserModel createdUser = userService.createUser(userRegisterDTO);

        assertNotNull(createdUser);
        assertEquals(userRegisterDTO.email(), createdUser.getEmail());
        assertTrue(new BCryptPasswordEncoder().matches("password", createdUser.getPassword()));
    }

    @Test
    void updateUser_UserNotFound_ThrowsException() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("New Name", null, null, null, null);

        when(userRepository.findById(anyString())).thenReturn(java.util.Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.updateUser(userUpdateDTO, "non-existent-id");
        });

        assertEquals("User not found.", exception.getMessage());
    }

    @Test
    void updateUser_Successful_UpdatesUser() {
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO("New Name", "newemail@example.com", "newpassword", "1234567890", null);
        UserModel existingUser = new UserModel("id", "Test Name", "test@example.com", "password", null, true, null, Role.REGULAR_USER);
        when(userRepository.findById(anyString())).thenReturn(java.util.Optional.of(existingUser));
        when(userRepository.save(any(UserModel.class))).thenAnswer(i -> i.getArguments()[0]);

        UserDataDTO updatedUser = userService.updateUser(userUpdateDTO, "id");

        assertEquals("New Name", updatedUser.name());
        assertEquals("newemail@example.com", updatedUser.email());
    }

    @Test
    void findByEmail_UserExists_ReturnsUserDataDTO() {
        UserModel existingUser = new UserModel("id", "Test Name", "test@example.com", "password", null, true, null, Role.REGULAR_USER);
        when(userRepository.findByEmail(anyString())).thenReturn(existingUser);

        UserDataDTO foundUser = userService.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("Test Name", foundUser.name());
        assertEquals("test@example.com", foundUser.email());
    }

    @Test
    void deleteUser_UserExists_DeletesUser() {
        doNothing().when(userRepository).deleteById(anyString());
        userService.deleteUser("id");

        verify(userRepository, times(1)).deleteById("id");
    }

    @Test
    void findActiveUsers_ReturnsActiveUsers() {
        Pageable pageable = Pageable.unpaged();
        when(userRepository.findByActive(eq(true), any(Pageable.class))).thenReturn(Page.empty());

        Page<UserDataDTO> activeUsers = userService.findActiveUsers(pageable);

        assertNotNull(activeUsers);
        assertTrue(activeUsers.isEmpty());
    }

    @Test
    void findInactiveUsers_ReturnsInactiveUsers() {
        Pageable pageable = Pageable.unpaged();
        when(userRepository.findByActive(eq(false), any(Pageable.class))).thenReturn(Page.empty());

        Page<UserDataDTO> inactiveUsers = userService.findInactiveUsers(pageable);

        assertNotNull(inactiveUsers);
        assertTrue(inactiveUsers.isEmpty());
    }
}
