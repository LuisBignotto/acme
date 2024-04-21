package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.users.dto.UserBaggageResponseDTO;
import br.com.acmeairlines.domain.users.dto.UserDataDTO;
import br.com.acmeairlines.domain.users.dto.UserRegisterDTO;
import br.com.acmeairlines.domain.users.dto.UserUpdateDTO;
import br.com.acmeairlines.domain.users.model.UserModel;
import br.com.acmeairlines.domain.users.service.UserService;
import br.com.acmeairlines.domain.baggages.service.BaggageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BaggageService baggageService;

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Page<UserDataDTO>> getActiveUsers(@PageableDefault(size = 10, sort = {"id"}) Pageable pages) {
        return ResponseEntity.ok(userService.findActiveUsers(pages));
    }

    @GetMapping("/inactive")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Page<UserDataDTO>> getInactiveUsers(@PageableDefault(size = 10, sort = {"id"}) Pageable pages) {
        return ResponseEntity.ok(userService.findInactiveUsers(pages));
    }

    @GetMapping("/{email}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<UserDataDTO> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(userService.findByEmail(email));
    }

    @GetMapping
    public ResponseEntity<UserBaggageResponseDTO> getUser(HttpServletRequest request) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        List<BaggageModel> baggage = baggageService.findByUserId(user.id());

        return ResponseEntity.ok(new UserBaggageResponseDTO(user, baggage));
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<UserDataDTO> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateDTO data) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        UserDataDTO newUser = userService.updateUser(data, user.id());

        if(newUser == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(newUser);
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity deleteUser(HttpServletRequest request){
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        userService.deleteUser(user.id());
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    @Transactional
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<UserDataDTO> registerWorker(@RequestBody @Valid UserRegisterDTO data){
        UserDataDTO email = userService.findByEmail(data.email());
        UserDataDTO cpf = userService.findByCpf(data.cpf());

        if(email != null && cpf != null) {
            return ResponseEntity.badRequest().build();
        }

        UserModel newUser = userService.createUser(data);

        return new ResponseEntity<>(new UserDataDTO(newUser), HttpStatus.CREATED);
    }
}
