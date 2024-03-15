package br.com.acmeairlines.controller;

import br.com.acmeairlines.domain.baggages.*;
import br.com.acmeairlines.domain.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BaggageRegisterControllerTest {

    @Mock
    private BaggageRepository baggageRepository;

    @InjectMocks
    private BaggageRegisterController baggageRegisterController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerBaggage() {
        BaggageRegisterData registerData = new BaggageRegisterData(
                1L,
                "TestTag",
                "Red",
                10.0,
                "Aeroporto Internacional de São Paulo",
                "Despachada",
                1L
        );

        BaggageModel newBaggage = new BaggageModel(registerData);
        when(baggageRepository.save(any(BaggageModel.class))).thenReturn(newBaggage);

        ResponseEntity<BaggageDataRecord> response = baggageRegisterController.registerBaggage(registerData);

        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(newBaggage.getId(), response.getBody().id());
        verify(baggageRepository, times(1)).save(any(BaggageModel.class));
    }

    @Test
    void getBaggage() {
        Long baggageId = 1L;
        BaggageModel existingBaggage = new BaggageModel();
        existingBaggage.setId(baggageId);

        when(baggageRepository.findById(baggageId)).thenReturn(Optional.of(existingBaggage));

        ResponseEntity response = baggageRegisterController.getBaggage(baggageId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(baggageId, ((BaggageModel) response.getBody()).getId());
    }

    @Test
    void updateBaggage() {
        Long baggageId = 1L;
        BaggageUpdateData updateData = new BaggageUpdateData(
                1L,
                "Despachada",
                "Aeroporto Internacional de São Paulo",
                2L
        );

        BaggageModel existingBaggage = new BaggageModel();
        existingBaggage.setId(baggageId);

        when(baggageRepository.findById(baggageId)).thenReturn(Optional.of(existingBaggage));

        ResponseEntity<BaggageModel> response = baggageRegisterController.updateBaggage(updateData, baggageId);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(updateData.status(), response.getBody().getStatus());
        assertEquals(updateData.lastSeenLocation(), response.getBody().getLastSeenLocation());
        assertEquals(updateData.flightId(), response.getBody().getFlightId());
    }

    @Test
    void deleteBaggage() {
        Long baggageId = 1L;
        BaggageModel existingBaggage = new BaggageModel();
        existingBaggage.setId(baggageId);

        when(baggageRepository.findById(baggageId)).thenReturn(Optional.of(existingBaggage));

        ResponseEntity response = baggageRegisterController.deleteBaggage(baggageId);

        assertEquals(204, response.getStatusCodeValue());
        verify(baggageRepository, times(1)).delete(any(BaggageModel.class));
    }
}
