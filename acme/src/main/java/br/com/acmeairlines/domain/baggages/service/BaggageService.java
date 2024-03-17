package br.com.acmeairlines.domain.baggages.service;

import br.com.acmeairlines.domain.baggages.dto.BaggageDTO;
import br.com.acmeairlines.domain.baggages.dto.BaggageUpdateDTO;
import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.repository.BaggageRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class BaggageService {

    @Autowired
    private BaggageRepository repository;

    public BaggageModel registerBaggage(BaggageDTO data){
        BaggageModel baggage = new BaggageModel(UUID.randomUUID().toString(), data.userId(), data.tag(), data.color(), data.weight(), data.status(), data.lastSeenLocation(), data.flightId());
        return repository.save(baggage);
    }

    public BaggageModel updateBaggage(@Valid BaggageUpdateDTO data, String id) {
        BaggageModel baggage = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("None baggages were found using id: " + id));

        if (data.status() != null) {
            baggage.setStatus(data.status());
        }

        if (data.lastSeenLocation() != null) {
            baggage.setLastSeenLocation(data.lastSeenLocation());
        }

        if (data.flightId() != null) {
            baggage.setFlightId(data.flightId());
        }

        return repository.save(baggage);
    }

    public List<BaggageModel> findByUserId(String id){
        List<BaggageModel> baggages = repository.findByUserId(id);
        return baggages;
    }

    public BaggageModel findById(String id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("None baggage were found using id: " + id));
    }

    public boolean deleteBaggage(String id, String userId){
        BaggageModel baggage = findById(id);
        if(baggage != null && userId.equalsIgnoreCase(baggage.getUserId())){
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BaggageModel> findBaggagesByFlightId(String flightId) {
        return repository.findByFlightId(flightId);
    }
}
