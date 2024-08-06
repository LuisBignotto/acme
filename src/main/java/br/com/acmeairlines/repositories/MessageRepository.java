package br.com.acmeairlines.repositories;

import br.com.acmeairlines.models.MessageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageModel, Long> {
    List<MessageModel> findByTicketId(Long ticketId);
}