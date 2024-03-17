package br.com.acmeairlines.domain.baggages.repository;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaggageRepository extends JpaRepository<BaggageModel, String> {
    List<BaggageModel> findByUserId(String userId);

    List<BaggageModel> findByFlightId(String id);

    BaggageModel findByTag(String tag);
}
