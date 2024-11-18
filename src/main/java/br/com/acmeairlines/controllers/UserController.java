package br.com.acmeairlines.controllers;

import br.com.acmeairlines.dtos.*;
import br.com.acmeairlines.helpers.RoleMapper;
import br.com.acmeairlines.models.UserModel;
import br.com.acmeairlines.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user-ms/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) {
        UserDTO user = userService.createUser(createUserDTO);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = userService.generateToken((UserModel) auth.getPrincipal());

        if (token == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String roleName = ((UserModel) auth.getPrincipal()).getRole().getRoleName();
        int roleId = RoleMapper.getRoleId(roleName);

        return ResponseEntity.ok(new TokenDTO(((UserModel) auth.getPrincipal()).getId(), token, String.valueOf(roleId)));
    }

    @GetMapping
    public ResponseEntity<Page<UserDTO>> findAllUsers(Pageable pageable) {
        Page<UserDTO> UserDTOs = userService.findAllUsers(pageable);
        return ResponseEntity.ok(UserDTOs);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getCompleteUserInfo(@RequestHeader("Authorization") String token) {
        String tokenWithoutBearer = token.substring(7);

        Map<String, Object> tokenResponse = userService.validateToken(tokenWithoutBearer);

        if (tokenResponse.containsKey("error")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long userId = Long.valueOf((String) tokenResponse.get("id"));
        UserResponseDTO user = userService.getCompleteUserInfo(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/role/{roleName}")
    public ResponseEntity<UserDTO> updateRoleOfUser(@PathVariable Long userId, @PathVariable String roleName) {
        UserDTO updatedUser = userService.updateRoleOfUser(userId, roleName);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public ResponseEntity<UserDTO> getUserByEmail(@RequestParam String email) {
        UserDTO user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UserDTO> getUserByCpf(@PathVariable String cpf) {
        UserDTO user = userService.getUserByCpf(cpf);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {
        UserDTO updatedUser = userService.updateUser(id, updateUserDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}