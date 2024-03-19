package br.com.acmeairlines.domain.flights.service;

import br.com.acmeairlines.domain.flights.dto.FlightDataDTO;
import br.com.acmeairlines.domain.flights.dto.FlightUpdateDTO;
import br.com.acmeairlines.domain.flights.model.FlightModel;
import br.com.acmeairlines.domain.flights.repository.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @InjectMocks
    private FlightService flightService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createFlight_Successful_CreatesFlight() {
        FlightDataDTO flightDataDTO = new FlightDataDTO(UUID.randomUUID().toString(),"FL123", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "JFK", "LAX");
        when(flightRepository.save(any(FlightModel.class))).thenAnswer(i -> i.getArguments()[0]);

        FlightModel createdFlight = flightService.createFlight(flightDataDTO);

        assertNotNull(createdFlight);
        assertEquals(flightDataDTO.flightNumber(), createdFlight.getFlightNumber());
    }

    @Test
    void updateFlight_FlightExists_UpdatesFlight() {
        FlightUpdateDTO flightUpdateDTO = new FlightUpdateDTO(LocalDateTime.now(), LocalDateTime.now().plusHours(3), "JFK", "SFO");
        FlightModel existingFlight = new FlightModel(UUID.randomUUID().toString(), "FL123", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "JFK", "LAX");
        when(flightRepository.findById(anyString())).thenReturn(Optional.of(existingFlight));
        when(flightRepository.save(any(FlightModel.class))).thenAnswer(i -> i.getArguments()[0]);

        FlightModel updatedFlight = flightService.updateFlight(flightUpdateDTO, existingFlight.getId());

        assertNotNull(updatedFlight);
        assertEquals(flightUpdateDTO.arrivalAirport(), updatedFlight.getArrivalAirport());
    }

    @Test
    void updateFlight_FlightNotFound_ThrowsException() {
        FlightUpdateDTO flightUpdateDTO = new FlightUpdateDTO(LocalDateTime.now(), LocalDateTime.now().plusHours(3), "JFK", "SFO");
        when(flightRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            flightService.updateFlight(flightUpdateDTO, "non-existent-id");
        });

        assertEquals("None flight were found using id: non-existent-id", exception.getMessage());
    }

    @Test
    void findAllFlights_ReturnsFlights() {
        Pageable pageable = Pageable.unpaged();
        FlightModel flight = new FlightModel(UUID.randomUUID().toString(), "FL123", LocalDateTime.now(), LocalDateTime.now().plusHours(2), "JFK", "LAX");
        Page<FlightModel> page = new PageImpl<>(Collections.singletonList(flight));
        when(flightRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<FlightModel> foundFlights = flightService.findAllFlights(pageable);

        assertFalse(foundFlights.isEmpty());
        assertEquals(1, foundFlights.getContent().size());
        assertEquals(flight, foundFlights.getContent().get(0));
    }
}
