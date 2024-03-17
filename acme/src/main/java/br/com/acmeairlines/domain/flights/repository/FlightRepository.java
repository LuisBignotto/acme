package br.com.acmeairlines.domain.flights.repository;

import br.com.acmeairlines.domain.flights.model.FlightModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightModel, String> {
    Page<FlightModel> findAll(Pageable pageable);
}
