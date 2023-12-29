package br.com.acmeairlines.controller;

import br.com.acmeairlines.users.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<Page<UserDataRecord>> getActiveUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pages) {
        var page = repository.findByActive(true, pages).map(UserDataRecord::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/inactive")
    public ResponseEntity<Page<UserDataRecord>> getInactiveUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pages) {
        var page = repository.findByActive(false, pages).map(UserDataRecord::new);
        return ResponseEntity.ok(page);
    }

    // PARSE UPPER CASE
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserModel>> getUsersByRole(@PathVariable Role role) {
        var users = repository.findByRole(role);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity getUserById(@PathVariable Long id) {
        var user = repository.findById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/search")
    public ResponseEntity<UserModel> getUserByEmail(@RequestParam String email) {
        var user = repository.findUserByEmail(email);
        return ResponseEntity.ok(user);
    }

//    @PostMapping("/register")
//    @Transactional
//    public ResponseEntity<UserDataRecord> cadastrar(@RequestBody @Valid UserRegisterData data) {
//        UserModel user = new UserModel(data);
//        user = repository.save(user);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(user.getId())
//                .toUri();
//        return ResponseEntity.created(location).body(new UserDataRecord(user));
//    }

    @PutMapping
    @Transactional
    public ResponseEntity<UserModel> updateUser(@RequestBody @Valid UserUpdateData data) {
        return repository.findById(data.id()).map(user -> {
                    user.updateUser(data);
                    return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteUser(@PathVariable Long id){
        return repository.findById(id).map(user -> {
                    repository.delete(user);
                    return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
