package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.dto.BaggageDTO;
import br.com.acmeairlines.domain.baggages.dto.BaggageUpdateDTO;
import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.service.BaggageService;
import br.com.acmeairlines.domain.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/baggage-register/baggage")
public class BaggageRegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private BaggageService baggageService;

    @PostMapping("/create")
    @Transactional
    public ResponseEntity<BaggageModel> registerBaggage(@RequestBody @Valid BaggageDTO data) {
        BaggageModel baggage = baggageService.registerBaggage(data);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(baggage.getId())
                .toUri();
        return ResponseEntity.created(location).body(baggage);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaggageModel> getBaggage(@PathVariable String id) {
        BaggageModel baggage = baggageService.findById(id);
        if(baggage != null) {
            return ResponseEntity.ok(baggage);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<BaggageModel> updateBaggage(@RequestBody @Valid BaggageUpdateDTO data, @PathVariable String id) {
        BaggageModel updatedBaggage = baggageService.updateBaggage(data, id);
        if(updatedBaggage != null) {
            return ResponseEntity.ok(updatedBaggage);
        }
        return ResponseEntity.notFound().build();
    }
}
