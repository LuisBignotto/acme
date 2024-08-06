package br.com.acmeairlines.repositories;

import br.com.acmeairlines.models.FlightModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FlightRepository extends JpaRepository<FlightModel, Long> {
    Optional<FlightModel> findByTag(String tag);
}