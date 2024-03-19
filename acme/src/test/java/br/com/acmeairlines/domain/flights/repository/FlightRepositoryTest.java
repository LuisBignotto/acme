package br.com.acmeairlines.domain.flights.repository;

import br.com.acmeairlines.domain.flights.model.FlightModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class FlightRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        FlightModel flight1 = new FlightModel(
                "1",
                "FL123",
                LocalDateTime.of(2023, 4, 15, 10, 30),
                LocalDateTime.of(2023, 4, 15, 14, 30),
                "JFK",
                "LAX"
        );
        FlightModel flight2 = new FlightModel(
                "2",
                "FL456",
                LocalDateTime.of(2023, 4, 16, 15, 0),
                LocalDateTime.of(2023, 4, 16, 19, 0),
                "LAX",
                "JFK"
        );
        entityManager.persist(flight1);
        entityManager.persist(flight2);
        entityManager.flush();
    }

    @Test
    void findAll_WhenRecordsExist_ReturnsNonEmptyPage() {
        Page<FlightModel> flights = flightRepository.findAll(PageRequest.of(0, 10));

        assertFalse(flights.isEmpty());
        assertTrue(flights.getContent().size() >= 2);
    }

    @Test
    void findAll_WhenPageIsTooHigh_ReturnsEmptyPage() {
        Page<FlightModel> flights = flightRepository.findAll(PageRequest.of(10, 10));

        assertTrue(flights.isEmpty());
    }
}
