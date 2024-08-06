package br.com.acmeairlines.repositories;

import br.com.acmeairlines.models.BaggageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaggageRepository extends JpaRepository<BaggageModel, Long> {
    List<BaggageModel> findByUserId(Long userId);

    List<BaggageModel> findByFlightId(Long id);

    BaggageModel findByTag(String tag);

    List<BaggageModel> findByTrackersTrackerUserId(Long trackerUserId);

}