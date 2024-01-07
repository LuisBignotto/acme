package br.com.acmeairlines.flights;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightModel, Long> {
    Page<FlightModel> findAll(Pageable pageable);
}
