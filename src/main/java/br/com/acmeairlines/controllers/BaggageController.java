package br.com.acmeairlines.controllers;

import br.com.acmeairlines.dtos.BaggageDTO;
import br.com.acmeairlines.dtos.CreateBaggageDTO;
import br.com.acmeairlines.helpers.LabelGeneratorService;
import br.com.acmeairlines.services.BaggageService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.google.zxing.WriterException;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/baggage-ms/baggages")
public class BaggageController {

    @Autowired
    private BaggageService baggageService;

    @Autowired
    private LabelGeneratorService labelGeneratorService;

    @PostMapping
    public ResponseEntity<BaggageDTO> createBaggage(@Valid @RequestBody CreateBaggageDTO createBaggageDTO) {
        BaggageDTO createdBaggage = baggageService.createBaggage(createBaggageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdBaggage);
    }

    @GetMapping
    public ResponseEntity<Page<BaggageDTO>> findAllFlights(Pageable pageable) {
        Page<BaggageDTO> BaggageDTOs = baggageService.findAllBaggages(pageable);
        return ResponseEntity.ok(BaggageDTOs);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BaggageDTO>> getBaggagesByUserId(@PathVariable Long userId) {
        List<BaggageDTO> baggages = baggageService.getBaggagesByUserId(userId);
        return ResponseEntity.ok(baggages);
    }

    @GetMapping("/flight/{flightId}")
    public ResponseEntity<List<BaggageDTO>> getBaggagesByFlightId(@PathVariable Long flightId) {
        List<BaggageDTO> baggages = baggageService.getBaggagesByFlightId(flightId);
        return ResponseEntity.ok(baggages);
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<BaggageDTO> getBaggageByTag(@PathVariable String tag) {
        BaggageDTO baggage = baggageService.getBaggageByTag(tag);
        if (baggage != null) {
            return ResponseEntity.ok(baggage);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tracker/{trackerUserId}")
    public ResponseEntity<List<BaggageDTO>> getBaggagesByTrackerUserId(@PathVariable Long trackerUserId) {
        List<BaggageDTO> baggages = baggageService.getBaggagesByTrackerUserId(trackerUserId);
        return ResponseEntity.ok(baggages);
    }

    @DeleteMapping("/{baggageId}")
    public ResponseEntity<Void> deleteBaggage(@PathVariable Long baggageId) {
        baggageService.deleteBaggage(baggageId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{baggageId}")
    public ResponseEntity<BaggageDTO> updateBaggage(@PathVariable Long baggageId, @Valid @RequestBody BaggageDTO baggageDTO) {
        BaggageDTO updatedBaggage = baggageService.updateBaggage(baggageId, baggageDTO);
        return ResponseEntity.ok(updatedBaggage);
    }

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateLabel(@RequestParam Long id,
                                                @RequestParam Long userId,
                                                @RequestParam String tag,
                                                @RequestParam String color,
                                                @RequestParam BigDecimal weight,
                                                @RequestParam Long flightId) {
        try {
            byte[] pdfBytes = labelGeneratorService.generateLabel(id, userId, tag, color, weight, flightId);

            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Disposition", "attachment; filename=label.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (WriterException | IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}