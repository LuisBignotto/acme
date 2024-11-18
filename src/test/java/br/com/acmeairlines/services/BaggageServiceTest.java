package br.com.acmeairlines.services;

import br.com.acmeairlines.dtos.BaggageDTO;
import br.com.acmeairlines.dtos.CreateBaggageDTO;
import br.com.acmeairlines.dtos.StatusDTO;
import br.com.acmeairlines.models.BaggageModel;
import br.com.acmeairlines.models.StatusModel;
import br.com.acmeairlines.repositories.BaggageRepository;
import br.com.acmeairlines.repositories.StatusRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BaggageServiceTest {

    @InjectMocks
    private BaggageService baggageService;

    @Mock
    private BaggageRepository baggageRepository;

    @Mock
    private StatusRepository statusRepository;

    private BaggageModel mockBaggage;
    private StatusModel mockStatus;

    @BeforeEach
    void setUp() {
        mockStatus = new StatusModel(1L, "Checked In");

        mockBaggage = new BaggageModel();
        mockBaggage.setId(1L);
        mockBaggage.setUserId(100L);
        mockBaggage.setTag("BAG123");
        mockBaggage.setColor("Red");
        mockBaggage.setWeight(new BigDecimal("20.5"));
        mockBaggage.setStatus(mockStatus);
        mockBaggage.setLastLocation("JFK Airport");
        mockBaggage.setFlightId(200L);
        mockBaggage.setTrackers(Collections.emptyList());
    }

    @Test
    void testFindAllBaggages() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(baggageRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of(mockBaggage)));

        Page<BaggageDTO> result = baggageService.findAllBaggages(pageable);
        assertEquals(1, result.getTotalElements());
        assertEquals("BAG123", result.getContent().get(0).tag());

        verify(baggageRepository, times(1)).findAll(pageable);
    }

    @Test
    void testCreateBaggage() {
        CreateBaggageDTO createBaggageDTO = new CreateBaggageDTO(
                100L, "BAG123", "Red", new BigDecimal("20.5"),
                1L, "JFK Airport", 200L, Collections.emptyList());

        when(statusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(baggageRepository.save(any(BaggageModel.class))).thenReturn(mockBaggage);

        BaggageDTO result = baggageService.createBaggage(createBaggageDTO);
        assertNotNull(result);
        assertEquals("BAG123", result.tag());
        assertEquals("Checked In", result.status().status());

        verify(statusRepository, times(1)).findById(1L);
        verify(baggageRepository, times(1)).save(any(BaggageModel.class));
    }

    @Test
    void testGetBaggagesByUserId() {
        when(baggageRepository.findByUserId(100L)).thenReturn(List.of(mockBaggage));

        List<BaggageDTO> result = baggageService.getBaggagesByUserId(100L);
        assertEquals(1, result.size());
        assertEquals("BAG123", result.get(0).tag());

        verify(baggageRepository, times(1)).findByUserId(100L);
    }

    @Test
    void testUpdateBaggage() {
        BaggageDTO baggageDTO = new BaggageDTO(
                1L, 100L, "BAG124", "Blue", new BigDecimal("25.0"),
                new StatusDTO(1L, "Checked In"), "LAX Airport", 201L, Collections.emptyList());

        when(baggageRepository.findById(1L)).thenReturn(Optional.of(mockBaggage));
        when(statusRepository.findById(1L)).thenReturn(Optional.of(mockStatus));
        when(baggageRepository.save(any(BaggageModel.class))).thenReturn(mockBaggage);

        BaggageDTO result = baggageService.updateBaggage(1L, baggageDTO);
        assertNotNull(result);
        assertEquals("BAG124", result.tag());
        assertEquals("Blue", result.color());

        verify(baggageRepository, times(1)).findById(1L);
        verify(baggageRepository, times(1)).save(any(BaggageModel.class));
    }
}
