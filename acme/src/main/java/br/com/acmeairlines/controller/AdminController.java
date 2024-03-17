package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.service.BaggageService;
import br.com.acmeairlines.domain.flights.dto.FlightDataDTO;
import br.com.acmeairlines.domain.flights.dto.FlightUpdateDTO;
import br.com.acmeairlines.domain.flights.model.FlightModel;
import br.com.acmeairlines.domain.flights.service.FlightService;
import br.com.acmeairlines.domain.users.dto.UserDataDTO;
import br.com.acmeairlines.domain.users.dto.UserRegisterDTO;
import br.com.acmeairlines.domain.users.model.UserModel;
import br.com.acmeairlines.domain.users.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    @Autowired
    private BaggageService baggageService;

    @GetMapping("/users/active")
    public ResponseEntity<Page<UserDataDTO>> getActiveUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pages) {
        Page<UserDataDTO> page = userService.findActiveUsers(pages);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/users/inactive")
    public ResponseEntity<Page<UserDataDTO>> getInactiveUsers(@PageableDefault(size = 10, sort = {"name"}) Pageable pages) {
        Page<UserDataDTO> page = userService.findInactiveUsers(pages);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/flights")
    public ResponseEntity<Page<FlightModel>> getFlights(@PageableDefault(size = 10, sort = {"id"}) Pageable pages) {
        Page<FlightModel> page = flightService.findAllFlights(pages);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/flights/{id}")
    public ResponseEntity<List<BaggageModel>> getFlightBaggages(@PathVariable String id) {
        List<BaggageModel> baggages = baggageService.findBaggagesByFlightId(id);
        return ResponseEntity.ok(baggages);
    }

    @GetMapping("/baggage/{id}")
    public ResponseEntity<BaggageModel> getBaggage(@PathVariable String id) {
        BaggageModel baggage = baggageService.findById(id);
        return ResponseEntity.ok(baggage);
    }

    @PostMapping("/create-flight")
    @Transactional
    public ResponseEntity<FlightModel> createFlight(@RequestBody @Valid FlightDataDTO data) {
        FlightModel flight = flightService.createFlight(data);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(flight.getId())
                .toUri();

        return ResponseEntity.created(location).body(flight);
    }

    @PutMapping("/flights/{id}")
    @Transactional
    public ResponseEntity<FlightModel> updateFlight(@PathVariable String id, @RequestBody @Valid FlightUpdateDTO data) {
        FlightModel updatedFlight = flightService.updateFlight(data, id);
        return ResponseEntity.ok(updatedFlight);
    }

    @PostMapping("/register-worker")
    @Transactional
    public ResponseEntity<UserDataDTO> registerWorker(@RequestBody @Valid UserRegisterDTO data){
        UserDataDTO user = userService.findByEmail(data.email());

        if(user != null) {
            return ResponseEntity.badRequest().build();
        }

        UserModel newUser = userService.createUser(data);

        return new ResponseEntity<>(new UserDataDTO(newUser), HttpStatus.CREATED);
    }
}
