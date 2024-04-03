package br.com.acmeairlines.domain.baggages.service;

import br.com.acmeairlines.domain.baggages.dto.BaggageDTO;
import br.com.acmeairlines.domain.baggages.dto.BaggageUpdateDTO;
import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.repository.BaggageRepository;
import br.com.acmeairlines.domain.users.model.UserModel;
import br.com.acmeairlines.domain.users.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class BaggageService {

    @Autowired
    private BaggageRepository repository;

    @Autowired
    private UserRepository userRepository;

    public BaggageModel registerBaggage(BaggageDTO data){
        UserModel user = userRepository.findByEmail(data.userEmail());
        if (user == null) {
            throw new IllegalArgumentException("User not found with email: " + data.userEmail());
        }

        BaggageModel baggage = new BaggageModel(UUID.randomUUID().toString(), user.getId(), data.tag(), data.color(), data.weight(), data.status(), data.lastSeenLocation(), data.flightId());
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

    public boolean deleteBaggage(String id, String userEmail){
        BaggageModel baggage = findById(id);
        if(baggage != null && userEmail.equalsIgnoreCase(userRepository.findByEmail(userEmail).getId())){
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BaggageModel> findBaggagesByFlightId(String flightId) {
        return repository.findByFlightId(flightId);
    }

    public List<BaggageModel> findAllBaggages() {
        return repository.findAll();
    }
    public void deleteBaggage(String id) {
        BaggageModel baggage = findById(id);
        repository.delete(baggage);
    }
}
