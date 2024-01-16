package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.*;
import br.com.acmeairlines.domain.users.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("users")
public class UserController {
    @Autowired
    private UserRepository repository;
    @Autowired
    private BaggageRepository baggageRepository;
    @GetMapping
    public ResponseEntity<UserAndBaggageResponse> getUser(HttpServletRequest request) {
        var user = repository.findUserDataRecordByEmail(request.getRemoteUser());
        if (user != null) {
            var baggage = baggageRepository.findByUserId(user.id());
            var response = new UserAndBaggageResponse(user, baggage);
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/baggage/{id}")
    public ResponseEntity getBaggage(HttpServletRequest request, @PathVariable Long id) {
        var user = repository.findUserDataRecordByEmail(request.getRemoteUser());
        if (user != null){
            var baggage = baggageRepository.findById(id);
            if(baggage != null) {
                List<BaggageModel> userBaggages = baggage.stream().filter(b -> b.getUserId() == user.id()).collect(Collectors.toList());
                if(!baggage.isEmpty()){
                    return ResponseEntity.ok(userBaggages);
                }
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add/baggage")
    public ResponseEntity addBaggage(@RequestBody @Valid BaggageUpdateRequest updateRequest) {
        BaggageModel baggage = baggageRepository.findByTag(updateRequest.getTag());
        if(baggage != null){
            baggage.updateBaggage(updateRequest.getData());
            baggageRepository.save(baggage);
            return ResponseEntity.ok(baggage);
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

    @DeleteMapping("/baggage/{id}")
    @Transactional
    public ResponseEntity deleteBag(HttpServletRequest request, @PathVariable Long id) {
        var user = repository.findUserDataRecordByEmail(request.getRemoteUser());
        if(user != null) {
            var baggage = baggageRepository.findById(id);
            if(baggage != null) {
                List<BaggageModel> userBaggages = baggage.stream().filter(b -> b.getUserId() == user.id()).filter(b -> b.getId() == id).collect(Collectors.toList());
                if(!userBaggages.isEmpty()){
                    baggageRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                }
            }
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