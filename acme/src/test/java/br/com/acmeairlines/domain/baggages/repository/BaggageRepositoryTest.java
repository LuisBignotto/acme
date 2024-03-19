package br.com.acmeairlines.domain.baggages.repository;

import br.com.acmeairlines.domain.baggages.model.BaggageModel;
import br.com.acmeairlines.domain.baggages.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class BaggageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BaggageRepository baggageRepository;

    @BeforeEach
    void setUp() {
        BaggageModel baggage1 = new BaggageModel("1", "User1", "Tag1", "Red", 23.5, Status.DESPACHADA, "Airport A", "Flight1");
        BaggageModel baggage2 = new BaggageModel("2", "User1", "Tag2", "Blue", 20.0, Status.AGUARDANDO_RECOLETA, "Airport B", "Flight2");
        BaggageModel baggage3 = new BaggageModel("3", "User2", "Tag3", "Green", 25.0, Status.EXTRAVIADA, "Airport C", "Flight1");
        entityManager.persist(baggage1);
        entityManager.persist(baggage2);
        entityManager.persist(baggage3);
        entityManager.flush();
    }

    @Test
    void findByUserId_WhenUserExists_ReturnsBaggages() {
        List<BaggageModel> baggages = baggageRepository.findByUserId("User1");

        assertEquals(2, baggages.size());
    }

    @Test
    void findByFlightId_WhenFlightExists_ReturnsBaggages() {
        List<BaggageModel> baggages = baggageRepository.findByFlightId("Flight1");

        assertEquals(2, baggages.size());
    }

    @Test
    void findByTag_WhenTagExists_ReturnsBaggage() {
        BaggageModel baggage = baggageRepository.findByTag("Tag1");

        assertNotNull(baggage);
        assertEquals("User1", baggage.getUserId());
        assertEquals("Flight1", baggage.getFlightId());
        assertEquals("Tag1", baggage.getTag());
    }
}
