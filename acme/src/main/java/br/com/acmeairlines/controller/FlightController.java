//package br.com.acmeairlines.controller;
//
//import br.com.acmeairlines.baggages.*;
//import br.com.acmeairlines.flights.*;
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
//@RequestMapping("flights")
//public class FlightController {
//
//    @Autowired
//    private FlightRepository repository;
//    @Autowired
//    private BaggageRepository repositoryBaggage;
//
//    @GetMapping
//    public ResponseEntity<List<FlightModel>> getFlights() {
//        var page = repository.findAll();
//        return ResponseEntity.ok(page);
//    }
//    @GetMapping("/flights/{id}")
//    public ResponseEntity<List<BaggageModel>> getFlightBaggages(@PathVariable Long id) {
//        var page = repositoryBaggage.findByFlightId(id);
//        return ResponseEntity.ok(page);
//    }
//    @PostMapping("/create")
//    @Transactional
//    public ResponseEntity<FlightDataRecord> registerFlight(@RequestBody @Valid FlightData data) {
//        FlightModel baggage = new FlightModel(data);
//        baggage = repository.save(baggage);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(baggage.getId())
//                .toUri();
//        return ResponseEntity.created(location).body(new FlightDataRecord(baggage));
//    }
//    @PutMapping
//    @Transactional
//    public ResponseEntity<FlightModel> updateFlight(@RequestBody @Valid FlightUpdateData data) {
//        return repository.findById(data.id()).map(user -> {
//            user.updateFlight(data);
//            return ResponseEntity.ok(user);
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//    @DeleteMapping("/{id}")
//    @Transactional
//    public ResponseEntity deleteFlight(@PathVariable Long id){
//        return repository.findById(id).map(flight -> {
//            repository.delete(flight);
//            return ResponseEntity.noContent().build();
//        }).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//}
