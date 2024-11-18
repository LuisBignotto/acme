package br.com.acmeairlines.services;

import br.com.acmeairlines.dtos.*;
import br.com.acmeairlines.models.*;
import br.com.acmeairlines.repositories.RoleRepository;
import br.com.acmeairlines.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private UserModel mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserModel();
        mockUser.setId(1L);
        mockUser.setEmail("test@acmeairlines.com");
        mockUser.setName("Test User");
        mockUser.setCpf("12345678901");

        RoleModel role = new RoleModel();
        role.setRoleName("ROLE_USER");
        mockUser.setRole(role);

        AddressModel address = new AddressModel();
        address.setId(1L);
        address.setStreet("123 Main St");
        address.setNeighborhood("Downtown");
        address.setZipcode("12345");
        address.setNumber("101");
        address.setCity("Metropolis");
        address.setState("NY");
        mockUser.setAddress(address);

        lenient().when(userRepository.findByCpf(anyString())).thenReturn(Optional.empty());
    }


    @Test
    void testLoadUserByUsername_UserExists() {
        when(userRepository.findByEmail("test@acmeairlines.com")).thenReturn(Optional.of(mockUser));
        assertDoesNotThrow(() -> userService.loadUserByUsername("test@acmeairlines.com"));
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        when(userRepository.findByEmail("nonexistent@acmeairlines.com")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("nonexistent@acmeairlines.com"));
    }

    @Test
    void testCreateUser_EmailAlreadyExists() {
        CreateUserDTO createUserDTO = new CreateUserDTO("existing@acmeairlines.com", "123456", "12345678901", "Test Name");
        when(userRepository.findByEmail("existing@acmeairlines.com")).thenReturn(Optional.of(mockUser));
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(createUserDTO));
    }

    @Test
    void testCreateUser_Success() {
        CreateUserDTO createUserDTO = new CreateUserDTO("new@acmeairlines.com", "123456", "12345678901", "New User");

        when(userRepository.findByEmail("new@acmeairlines.com")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName("ROLE_USER")).thenReturn(Optional.of(new RoleModel()));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            user.setId(1L);
            user.setAddress(mockUser.getAddress());
            return user;
        });

        UserDTO result = userService.createUser(createUserDTO);
        assertEquals("new@acmeairlines.com", result.email());
        assertNotNull(result.address());
    }
}

