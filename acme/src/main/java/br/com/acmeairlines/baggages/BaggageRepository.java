package br.com.acmeairlines.baggages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaggageRepository extends JpaRepository<BaggageModel, Long> {
    BaggageModel findByUserId(Long id);
    List<BaggageModel> findByFlight(Long id);
}
