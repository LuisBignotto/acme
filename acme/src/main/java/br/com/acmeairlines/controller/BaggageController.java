package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.dto.BaggageDTO;
import br.com.acmeairlines.domain.baggages.dto.BaggageUpdateDTO;
import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.service.BaggageService;
import br.com.acmeairlines.domain.users.dto.UserDataDTO;
import br.com.acmeairlines.domain.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("baggages")
public class BaggageController {

    @Autowired
    private UserService userService;

    @Autowired
    private BaggageService baggageService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<BaggageModel> getBaggage(@PathVariable String id) {
        BaggageModel baggage = baggageService.findById(id);
        return ResponseEntity.ok(baggage);
    }

    @GetMapping("/tag/{tag}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<BaggageModel> getBaggageByTag(@PathVariable String tag) {
        BaggageModel baggage = baggageService.findByTag(tag);
        return ResponseEntity.ok(baggage);
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<List<BaggageModel>> getBaggageByEmail(@PathVariable String email) {
        List<BaggageModel> baggages = baggageService.findByEmail(email);
        return ResponseEntity.ok(baggages);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<List<BaggageModel>> getAllBaggages() {
        List<BaggageModel> baggages = baggageService.findAllBaggages();
        return ResponseEntity.ok(baggages);
    }

    @GetMapping("/add/{id}")
    public ResponseEntity<BaggageModel> getUserBaggage(HttpServletRequest request, @PathVariable String id) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        if (user != null){
            BaggageModel baggage = baggageService.findById(id);
            if(baggage != null && baggage.getUserId().equals(user.id())) {
                return ResponseEntity.ok(baggage);
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    public ResponseEntity<BaggageModel> addBaggage(HttpServletRequest request, @RequestBody @Valid BaggageUpdateDTO updateRequest) {
        UserDataDTO user = userService.findByEmail(request.getRemoteUser());
        BaggageModel baggage = baggageService.updateBaggage(updateRequest, user.id());
        if(baggage != null){
            return ResponseEntity.ok(baggage);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<BaggageModel> createBaggage(@RequestBody @Valid BaggageDTO data) {
        BaggageModel baggage = baggageService.registerBaggage(data);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(baggage.getId())
                .toUri();

        return ResponseEntity.created(location).body(baggage);
    }

    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<BaggageModel> updateBaggage(@RequestBody @Valid BaggageUpdateDTO data, @PathVariable String id) {
        BaggageModel updatedBaggage = baggageService.updateBaggage(data, id);

        if(updatedBaggage == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedBaggage);
    }

    @DeleteMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity deleteBaggage(@PathVariable String id) {
        baggageService.deleteBaggage(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/delete/{id}")
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
}
