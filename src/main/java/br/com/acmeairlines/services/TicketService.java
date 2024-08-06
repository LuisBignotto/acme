package br.com.acmeairlines.services;

import br.com.acmeairlines.dtos.UserDTO;
import br.com.acmeairlines.models.MessageModel;
import br.com.acmeairlines.models.TicketModel;
import br.com.acmeairlines.models.UserModel;
import br.com.acmeairlines.repositories.MessageRepository;
import br.com.acmeairlines.repositories.TicketRepository;
import br.com.acmeairlines.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    public TicketModel createTicket(Long userId, String title, String description) {
        TicketModel ticket = new TicketModel();
        ticket.setUserId(userId);
        ticket.setTitle(title);
        ticket.setDescription(description);
        ticket.setStatus("OPEN");
        ticket.setCreatedAt(LocalDateTime.now());
        ticket.setUpdatedAt(LocalDateTime.now());

        return ticketRepository.save(ticket);
    }

    public TicketModel updateTicket(Long ticketId, String status) {
        Optional<TicketModel> optionalTicket = ticketRepository.findById(ticketId);

        if (optionalTicket.isPresent()) {
            TicketModel ticket = optionalTicket.get();
            ticket.setStatus(status);
            ticket.setUpdatedAt(LocalDateTime.now());
            return ticketRepository.save(ticket);
        }

        throw new RuntimeException("TicketModel not found with id: " + ticketId);
    }

    public Optional<TicketModel> getTicketById(Long ticketId) {
        return ticketRepository.findById(ticketId);
    }

    public Page<TicketModel> getAllTickets(Pageable pageable) {
        return ticketRepository.findAll(pageable);
    }
    public void deleteTicket(Long ticketId) {
        if (ticketRepository.existsById(ticketId)) {
            ticketRepository.deleteById(ticketId);
        } else {
            throw new RuntimeException("TicketModel not found with id: " + ticketId);
        }
    }

    public MessageModel addMessage(Long ticketId, Long senderId, String messageText) {
        MessageModel message = new MessageModel();
        message.setTicketId(ticketId);
        message.setSenderId(senderId);
        message.setMessage(messageText);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<MessageModel> getMessagesByTicketId(Long ticketId) {
        return messageRepository.findByTicketId(ticketId);
    }

    public List<TicketModel> getUserById(String token, Long userId) {
        Optional<UserModel> user = userRepository.findById(userId);

        if(user.isEmpty()){
            throw new RuntimeException("User not found with id: " + userId);
        }

        return ticketRepository.findByUserId(user.get().getId());
    }
}