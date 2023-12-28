package br.com.acmeairlines.flights;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<FlightModel, Long> {
}
