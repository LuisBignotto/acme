package br.com.acmeairlines.controllers;

import br.com.acmeairlines.dtos.FlightCreateDTO;
import br.com.acmeairlines.dtos.FlightDTO;
import br.com.acmeairlines.dtos.FlightUpdateDTO;
import br.com.acmeairlines.services.FlightService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/flight-ms/flights")
public class FlightController {

    @Autowired
    private FlightService flightService;

    @PostMapping
    public ResponseEntity<FlightDTO> createFlight(@Valid @RequestBody FlightCreateDTO flightCreateDTO) {
        FlightDTO flightDTO = flightService.createFlight(flightCreateDTO);
        return ResponseEntity.created(null).body(flightDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightDTO> updateFlight(@Valid @RequestBody FlightUpdateDTO flightUpdateDTO, @PathVariable Long id) {
        return flightService.updateFlight(flightUpdateDTO, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable Long id) {
        return flightService.getFlightById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<FlightDTO> getFlightByTag(@RequestParam String tag) {
        return flightService.getFlightByTag(tag)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<FlightDTO>> findAllFlights(Pageable pageable) {
        Page<FlightDTO> flightDTOs = flightService.findAllFlights(pageable);
        return ResponseEntity.ok(flightDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }
}