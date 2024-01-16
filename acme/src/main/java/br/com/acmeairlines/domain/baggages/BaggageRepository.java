package br.com.acmeairlines.domain.baggages;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaggageRepository extends JpaRepository<BaggageModel, Long> {
    List<BaggageModel> findByUserId(Long id);
    List<BaggageModel> findByFlightId(Long id);
}
