//package br.com.acmeairlines.controller;
//
//import br.com.acmeairlines.baggages.*;
//import jakarta.validation.Valid;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
//
//import java.net.URI;
//import java.util.List;
//
//@RestController
//@RequestMapping("baggage")
//public class BaggageController {
//
//    @Autowired
//    private BaggageRepository repository;
//
//    @GetMapping
//    public ResponseEntity<BaggageModel> getBaggages(@RequestBody Long id) {
//        var page = repository.findByUserId(id);
//        return ResponseEntity.ok(page);
//    }
//    @GetMapping("/{id}")
//    public ResponseEntity getBaggage(@PathVariable Long id) {
//        var page = repository.findById(id);
//        return ResponseEntity.ok(page);
//    }
//    @PostMapping("/create")
//    @Transactional
//    public ResponseEntity<BaggageDataRecord> registerBaggage(@RequestBody @Valid BaggageRegisterData data) {
//        BaggageModel baggage = new BaggageModel(data);
//        baggage = repository.save(baggage);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(baggage.getId())
//                .toUri();
//        return ResponseEntity.created(location).body(new BaggageDataRecord(baggage));
//    }
//    @PutMapping
//    @Transactional
//    public ResponseEntity<BaggageModel> updateUser(@RequestBody @Valid BaggageUpdateData data) {
//        return repository.findById(data.id()).map(user -> {
//            user.updateBaggage(data);
//            return ResponseEntity.ok(user);
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//    @DeleteMapping("/{id}")
//    @Transactional
//    public ResponseEntity deleteBaggage(@PathVariable Long id){
//        return repository.findById(id).map(baggage -> {
//            repository.delete(baggage);
//            return ResponseEntity.noContent().build();
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//}
