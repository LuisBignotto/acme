package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.service.BaggageService;
import br.com.acmeairlines.domain.flights.dto.FlightBaggageResponseDTO;
import br.com.acmeairlines.domain.flights.dto.FlightDataDTO;
import br.com.acmeairlines.domain.flights.dto.FlightUpdateDTO;
import br.com.acmeairlines.domain.flights.model.FlightModel;
import br.com.acmeairlines.domain.flights.service.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @Autowired
    private BaggageService baggageService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<Page<FlightModel>> getFlights(@PageableDefault(size = 10, sort = {"id"}) Pageable pages) {
        return ResponseEntity.ok(flightService.findAllFlights(pages));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<FlightBaggageResponseDTO> getFlight(@PathVariable String id) {
        FlightDataDTO data = flightService.getFlight(id);
        List<BaggageModel> baggages = baggageService.findBaggagesByFlightId(id);
        return ResponseEntity.ok(new FlightBaggageResponseDTO(data, baggages));
    }

    @GetMapping("/tag/{tag}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<FlightBaggageResponseDTO> getFlightByTag(@PathVariable String tag) {
        FlightDataDTO data = flightService.getFlightByTag(tag);
        List<BaggageModel> baggages = baggageService.findBaggagesByFlightId(data.id());
        return ResponseEntity.ok(new FlightBaggageResponseDTO(data, baggages));
    }

    @PostMapping("/create")
    @Transactional
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<FlightModel> createFlight(@RequestBody @Valid FlightDataDTO data) {
        FlightModel flight = flightService.createFlight(data);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(flight.getId())
                .toUri();

        return ResponseEntity.created(location).body(flight);
    }

    @PutMapping("/{id}")
    @Transactional
    @PreAuthorize("hasAuthority('ADMINISTRATOR') or hasAuthority('BAGGAGE_MANAGER')")
    public ResponseEntity<FlightModel> updateFlight(@PathVariable String id, @RequestBody @Valid FlightUpdateDTO data) {
        return ResponseEntity.ok(flightService.updateFlight(data, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteFlight(@PathVariable String id) {
        List<BaggageModel> baggages = baggageService.findBaggagesByFlightId(id);
        if(baggages.isEmpty()){
            flightService.deleteFlight(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }
}
