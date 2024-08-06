package br.com.acmeairlines.repositories;

import br.com.acmeairlines.models.TicketModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<TicketModel, Long> {
    List<TicketModel> findByUserId(Long userId);
}