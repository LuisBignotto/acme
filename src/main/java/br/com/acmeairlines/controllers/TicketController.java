package br.com.acmeairlines.controllers;

import br.com.acmeairlines.dtos.MessageCreateDTO;
import br.com.acmeairlines.dtos.TicketCreateDTO;
import br.com.acmeairlines.dtos.UpdateStatusDTO;
import br.com.acmeairlines.models.MessageModel;
import br.com.acmeairlines.models.TicketModel;
import br.com.acmeairlines.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ticket-ms/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketModel> createTicket(@RequestBody TicketCreateDTO ticketCreateDTO) {
        TicketModel ticket = ticketService.createTicket(ticketCreateDTO.userId(), ticketCreateDTO.title(), ticketCreateDTO.description());
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketModel> updateTicket(@PathVariable Long ticketId, @RequestBody UpdateStatusDTO updateStatusDTO) {
        TicketModel updatedTicket = ticketService.updateTicket(ticketId, updateStatusDTO.status());
        return ResponseEntity.ok(updatedTicket);
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketModel> getTicketById(@PathVariable Long ticketId) {
        Optional<TicketModel> ticket = ticketService.getTicketById(ticketId);
        return ticket.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Page<TicketModel>> getAllTickets(Pageable pageable) {
        return ResponseEntity.ok(ticketService.getAllTickets(pageable));
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{ticketId}/messages")
    public ResponseEntity<MessageModel> addMessage(@PathVariable Long ticketId, @RequestBody MessageCreateDTO messageCreateDTO) {
        return ResponseEntity.ok(ticketService.addMessage(ticketId, messageCreateDTO.senderId(), messageCreateDTO.message()));
    }

    @GetMapping("/{ticketId}/messages")
    public ResponseEntity<List<MessageModel>> getMessagesByTicketId(@PathVariable Long ticketId) {
        return ResponseEntity.ok(ticketService.getMessagesByTicketId(ticketId));
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<List<TicketModel>> getUserById(@RequestHeader("Authorization") String token, @PathVariable Long userId) {
        return ResponseEntity.ok(ticketService.getUserById(token, userId));
    }
}