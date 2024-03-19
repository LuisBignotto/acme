package br.com.acmeairlines.domain.baggages.service;

import br.com.acmeairlines.domain.baggages.dto.BaggageDTO;
import br.com.acmeairlines.domain.baggages.dto.BaggageUpdateDTO;
import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.model.Status;
import br.com.acmeairlines.domain.baggages.repository.BaggageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BaggageServiceTest {

    @Mock
    private BaggageRepository baggageRepository;

    @InjectMocks
    private BaggageService baggageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBaggage_Successful_CreatesBaggage() {
        BaggageDTO baggageDTO = new BaggageDTO("id", "userId","tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId");
        when(baggageRepository.save(any(BaggageModel.class))).thenAnswer(i -> i.getArguments()[0]);

        BaggageModel createdBaggage = baggageService.registerBaggage(baggageDTO);

        assertNotNull(createdBaggage);
        assertEquals(baggageDTO.userId(), createdBaggage.getUserId());
        assertEquals(baggageDTO.weight(), createdBaggage.getWeight());
    }

    @Test
    void updateBaggage_BaggageExists_UpdatesBaggage() {
        BaggageUpdateDTO baggageUpdateDTO = new BaggageUpdateDTO(Status.DESPACHADA, "lastSeenLocation", "flightId");
        BaggageModel existingBaggage = new BaggageModel("id", "userId", "tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId");
        when(baggageRepository.findById(anyString())).thenReturn(Optional.of(existingBaggage));
        when(baggageRepository.save(any(BaggageModel.class))).thenAnswer(i -> i.getArguments()[0]);

        BaggageModel updatedBaggage = baggageService.updateBaggage(baggageUpdateDTO, existingBaggage.getId());

        assertNotNull(updatedBaggage);
        assertEquals(baggageUpdateDTO.status(), updatedBaggage.getStatus());
    }

    @Test
    void updateBaggage_BaggageNotFound_ThrowsException() {
        BaggageUpdateDTO baggageUpdateDTO = new BaggageUpdateDTO(Status.DESPACHADA, "lastSeenLocation", "flightId");
        when(baggageRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            baggageService.updateBaggage(baggageUpdateDTO, "non-existent-id");
        });

        assertEquals("None baggages were found using id: non-existent-id", exception.getMessage());
    }

    @Test
    void findByUserId_ReturnsBaggages() {
        List<BaggageModel> expectedBaggages = List.of(new BaggageModel("id", "userId", "tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId"));
        when(baggageRepository.findByUserId(anyString())).thenReturn(expectedBaggages);

        List<BaggageModel> foundBaggages = baggageService.findByUserId("userId");

        assertFalse(foundBaggages.isEmpty());
        assertEquals(expectedBaggages.size(), foundBaggages.size());
        assertEquals(expectedBaggages.get(0).getId(), foundBaggages.get(0).getId());
    }

    @Test
    void findById_BaggageExists_ReturnsBaggage() {
        BaggageModel expectedBaggage = new BaggageModel("id", "userId", "tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId");
        when(baggageRepository.findById(anyString())).thenReturn(Optional.of(expectedBaggage));

        BaggageModel foundBaggage = baggageService.findById("id");

        assertNotNull(foundBaggage);
        assertEquals(expectedBaggage.getId(), foundBaggage.getId());
    }

    @Test
    void findById_BaggageNotFound_ThrowsException() {
        when(baggageRepository.findById(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            baggageService.findById("non-existent-id");
        });

        assertEquals("None baggage were found using id: non-existent-id", exception.getMessage());
    }

    @Test
    void deleteBaggage_BaggageExistsAndUserMatches_DeletesBaggage() {
        BaggageModel existingBaggage = new BaggageModel("id", "userId", "tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId");
        when(baggageRepository.findById(anyString())).thenReturn(Optional.of(existingBaggage));
        doNothing().when(baggageRepository).deleteById(anyString());

        boolean result = baggageService.deleteBaggage("id", "userId");

        assertTrue(result);
        verify(baggageRepository, times(1)).deleteById("id");
    }

    @Test
    void deleteBaggage_BaggageExistsAndUserDoesNotMatch_DoesNotDeleteBaggage() {
        BaggageModel existingBaggage = new BaggageModel("id", "userId", "tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId");
        when(baggageRepository.findById(anyString())).thenReturn(Optional.of(existingBaggage));

        boolean result = baggageService.deleteBaggage("id", "differentUserId");

        assertFalse(result);
        verify(baggageRepository, never()).deleteById("id");
    }

    @Test
    void findBaggagesByFlightId_ReturnsBaggages() {
        List<BaggageModel> expectedBaggages = List.of(new BaggageModel("id", "userId", "tag", "color", 20.0, Status.DESPACHADA, "lastSeenLocation", "flightId"));
        when(baggageRepository.findByFlightId(anyString())).thenReturn(expectedBaggages);

        List<BaggageModel> foundBaggages = baggageService.findBaggagesByFlightId("flightId");

        assertFalse(foundBaggages.isEmpty());
        assertEquals(expectedBaggages.size(), foundBaggages.size());
        assertEquals(expectedBaggages.get(0).getId(), foundBaggages.get(0).getId());
    }

}
