package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.BaggageModel;
import br.com.acmeairlines.domain.baggages.BaggageRepository;
import br.com.acmeairlines.domain.flights.*;
import br.com.acmeairlines.domain.users.UserDataRecord;
import br.com.acmeairlines.domain.users.UserModel;
import br.com.acmeairlines.domain.users.UserRegisterData;
import br.com.acmeairlines.domain.users.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("admin")
public class AdminController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private BaggageRepository baggageRepository;

    @GetMapping("/users/active")
    public ResponseEntity<Page<UserDataRecord>> getActiveUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pages) {
        var page = userRepository.findByActive(true, pages).map(UserDataRecord::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<Page<UserDataRecord>> getInactiveUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pages) {
        var page = userRepository.findByActive(false, pages).map(UserDataRecord::new);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/flights")
    public ResponseEntity<Page<FlightModel>> getFlights(@PageableDefault(size = 10, sort = {"id"}) Pageable pages) {
        var page = flightRepository.findAll(pages);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<List<BaggageModel>> getFlightBaggages(@PathVariable Long id) {
        var page = baggageRepository.findByFlightId(id);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/baggage/{id}")
    public ResponseEntity getBaggage(@PathVariable Long id) {
        Optional<BaggageModel> baggage = baggageRepository.findById(id);
        if (baggage.isPresent()) {
            return ResponseEntity.ok(baggage.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-flight")
    @Transactional
    public ResponseEntity<FlightDataRecord> createFlight(@RequestBody @Valid FlightData data) {
        FlightModel flight = new FlightModel(data);
        flight = flightRepository.save(flight);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(flight.getId())
                .toUri();
        return ResponseEntity.created(location).body(new FlightDataRecord(flight));
    }

    @PutMapping("/flights/{id}")
    @Transactional
    public ResponseEntity<FlightModel> updateFlight(@PathVariable Long id, @RequestBody @Valid FlightUpdateData data) {
        return flightRepository.findById(id).map(user -> {
            user.updateFlight(data);
            return ResponseEntity.ok(user);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/register-worker")
    @Transactional
    public ResponseEntity registerWorker(@RequestBody @Valid UserRegisterData data){
        if(this.userRepository.findByEmail(data.email()) != null) return ResponseEntity.badRequest().build();

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword, data.role(), data.active());

        this.userRepository.save(newUser);

        return ResponseEntity.ok().build();
    }
}
