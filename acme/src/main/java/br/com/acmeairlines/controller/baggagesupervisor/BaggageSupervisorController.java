package br.com.acmeairlines.controller.baggagesupervisor;

import br.com.acmeairlines.baggages.BaggageModel;
import br.com.acmeairlines.baggages.BaggageRepository;
import br.com.acmeairlines.baggages.BaggageUpdateData;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("supervisor/baggages")
public class BaggageSupervisorController {
    @Autowired
    private BaggageRepository baggageRepository;
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
}
