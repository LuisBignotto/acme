package br.com.acmeairlines.controller.baggageregister;

import br.com.acmeairlines.baggages.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("baggage")
public class BaggageRegisterController {
    @Autowired
    private BaggageRepository baggageRepository;
    @PostMapping("/create")
    @Transactional
    public ResponseEntity<BaggageDataRecord> registerBaggage(@RequestBody @Valid BaggageRegisterData data) {
        BaggageModel baggage = new BaggageModel(data);
        baggage = baggageRepository.save(baggage);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(baggage.getId())
                .toUri();
        return ResponseEntity.created(location).body(new BaggageDataRecord(baggage));
    }
    @GetMapping("/{id}")
    public ResponseEntity getBaggage(@PathVariable Long id) {
        var page = baggageRepository.findById(id);
        return ResponseEntity.ok(page);
    }
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<BaggageModel> updateBaggage(@RequestBody @Valid BaggageUpdateData data, @PathVariable Long id) {
        return baggageRepository.findById(id).map(user -> {
            user.updateBaggage(data);
            return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity deleteBaggage(@PathVariable Long id){
        return baggageRepository.findById(id).map(baggage -> {
            baggageRepository.delete(baggage);
            return ResponseEntity.noContent().build();
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
