package br.com.acmeairlines.controller.user;

import br.com.acmeairlines.address.Address;
import br.com.acmeairlines.users.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository repository;
    @GetMapping
    public ResponseEntity getUser(HttpServletRequest request) {
        var user = (UserDataRecord) repository.findUserDataRecordByEmail(request.getRemoteUser());
        if(user != null) {
            return ResponseEntity.ok(user);
        }
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/update")
    @Transactional
    public ResponseEntity updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateData data) {
        var user = (UserModel) repository.findUserByEmail(request.getRemoteUser());
        if(user != null) {
            user.updateUser(data);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
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