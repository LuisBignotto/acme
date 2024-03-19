package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.dto.BaggageUpdateDTO;
import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.users.dto.UserBaggageResponseDTO;
import br.com.acmeairlines.domain.users.dto.UserDataDTO;
import br.com.acmeairlines.domain.users.dto.UserUpdateDTO;
import br.com.acmeairlines.domain.users.service.UserService;
import br.com.acmeairlines.domain.baggages.service.BaggageService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    @GetMapping
    public ResponseEntity<UserBaggageResponseDTO> getUser(HttpServletRequest request) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        if (user != null) {
            List<BaggageModel> baggage = baggageService.findByUserId(user.id());
            UserBaggageResponseDTO response = new UserBaggageResponseDTO(user, baggage);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/baggage/{id}")
    public ResponseEntity<BaggageModel> getBaggage(HttpServletRequest request, @PathVariable String id) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        if (user != null){
            BaggageModel baggage = baggageService.findById(id);
            if(baggage != null && baggage.getUserId().equals(user.id())) {
                return ResponseEntity.ok(baggage);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add/baggage")
    public ResponseEntity<BaggageModel> addBaggage(HttpServletRequest request, @RequestBody @Valid BaggageUpdateDTO updateRequest) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        BaggageModel baggage = baggageService.updateBaggage(updateRequest, user.id());
        if(baggage != null){
            return ResponseEntity.ok(baggage);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/update")
    @Transactional
    public ResponseEntity<UserDataDTO> updateUser(HttpServletRequest request, @RequestBody @Valid UserUpdateDTO data) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        UserDataDTO newUser = userService.updateUser(data, user.id());
        if(newUser != null) {
            return ResponseEntity.ok(newUser);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/baggage/{id}")
    @Transactional
    public ResponseEntity deleteBag(HttpServletRequest request, @PathVariable String id) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        if(user != null) {
            boolean result = baggageService.deleteBaggage(id, user.id());
            if(result) {
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping
    @Transactional
    public ResponseEntity deleteUser(HttpServletRequest request){
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        userService.deleteUser(user.id());
        return ResponseEntity.noContent().build();
    }
}
