package br.com.acmeairlines.services;

import br.com.acmeairlines.dtos.FlightCreateDTO;
import br.com.acmeairlines.dtos.FlightDTO;
import br.com.acmeairlines.dtos.FlightUpdateDTO;
import br.com.acmeairlines.models.FlightModel;
import br.com.acmeairlines.repositories.FlightRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @InjectMocks
    private FlightService flightService;

    @Mock
    private FlightRepository repository;

    private FlightModel mockFlight;

    @BeforeEach
    void setUp() {
        mockFlight = new FlightModel(
                1L,
                "FL123",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "JFK",
                "LAX",
                "SCHEDULED",
                "Boeing 737"
        );
    }

    @Test
    void testCreateFlight() {
        FlightCreateDTO createDTO = new FlightCreateDTO(
                "FL123",
                LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2),
                "JFK",
                "LAX",
                "SCHEDULED",
                "Boeing 737"
        );

        when(repository.save(any(FlightModel.class))).thenReturn(mockFlight);

        FlightDTO result = flightService.createFlight(createDTO);

        assertNotNull(result);
        assertEquals("FL123", result.tag());
        verify(repository, times(1)).save(any(FlightModel.class));
    }

    @Test
    void testUpdateFlight_FlightExists() {
        FlightUpdateDTO updateDTO = new FlightUpdateDTO(
                "FL456",
                null,
                null,
                null,
                null,
                "DELAYED",
                null
        );

        when(repository.findById(1L)).thenReturn(Optional.of(mockFlight));
        when(repository.save(any(FlightModel.class))).thenReturn(mockFlight);

        Optional<FlightDTO> result = flightService.updateFlight(updateDTO, 1L);

        assertTrue(result.isPresent());
        assertEquals("DELAYED", result.get().status());
        verify(repository, times(1)).save(mockFlight);
    }

    @Test
    void testUpdateFlight_FlightNotFound() {
        FlightUpdateDTO updateDTO = new FlightUpdateDTO("FL456", null, null, null, null, "DELAYED", null);

        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<FlightDTO> result = flightService.updateFlight(updateDTO, 1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetFlightById_FlightExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(mockFlight));

        Optional<FlightDTO> result = flightService.getFlightById(1L);

        assertTrue(result.isPresent());
        assertEquals("FL123", result.get().tag());
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void testGetFlightById_FlightNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        Optional<FlightDTO> result = flightService.getFlightById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetFlightByTag_FlightExists() {
        when(repository.findByTag("FL123")).thenReturn(Optional.of(mockFlight));

        Optional<FlightDTO> result = flightService.getFlightByTag("FL123");

        assertTrue(result.isPresent());
        assertEquals("FL123", result.get().tag());
        verify(repository, times(1)).findByTag("FL123");
    }

    @Test
    void testGetFlightByTag_FlightNotFound() {
        when(repository.findByTag("FL123")).thenReturn(Optional.empty());

        Optional<FlightDTO> result = flightService.getFlightByTag("FL123");

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAllFlights() {
        Page<FlightModel> flightPage = new PageImpl<>(List.of(mockFlight));
        when(repository.findAll(any(Pageable.class))).thenReturn(flightPage);

        Page<FlightDTO> result = flightService.findAllFlights(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(repository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testDeleteFlight() {
        doNothing().when(repository).deleteById(1L);

        assertDoesNotThrow(() -> flightService.deleteFlight(1L));
        verify(repository, times(1)).deleteById(1L);
    }
}
