package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.users.UserDataRecord;
import br.com.acmeairlines.domain.users.UserModel;
import br.com.acmeairlines.domain.users.UserRepository;
import br.com.acmeairlines.domain.users.UserUpdateData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @DeleteMapping
    @Transactional
    public ResponseEntity deleteUser(HttpServletRequest request){
        var user = (UserModel) repository.findUserByEmail(request.getRemoteUser());
        repository.delete(user);
        return ResponseEntity.noContent().build();
    }
}