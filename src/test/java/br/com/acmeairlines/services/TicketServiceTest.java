package br.com.acmeairlines.services;

import br.com.acmeairlines.models.MessageModel;
import br.com.acmeairlines.models.TicketModel;
import br.com.acmeairlines.models.UserModel;
import br.com.acmeairlines.repositories.MessageRepository;
import br.com.acmeairlines.repositories.TicketRepository;
import br.com.acmeairlines.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @InjectMocks
    private TicketService ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    private TicketModel mockTicket;
    private MessageModel mockMessage;

    @BeforeEach
    void setUp() {
        mockTicket = new TicketModel();
        mockTicket.setId(1L);
        mockTicket.setUserId(1L);
        mockTicket.setTitle("Sample Ticket");
        mockTicket.setDescription("Description");
        mockTicket.setStatus("OPEN");
        mockTicket.setCreatedAt(LocalDateTime.now());
        mockTicket.setUpdatedAt(LocalDateTime.now());

        mockMessage = new MessageModel();
        mockMessage.setId(1L);
        mockMessage.setTicketId(1L);
        mockMessage.setSenderId(1L);
        mockMessage.setMessage("Sample Message");
        mockMessage.setTimestamp(LocalDateTime.now());
    }

    @Test
    void testCreateTicket() {
        when(ticketRepository.save(any(TicketModel.class))).thenReturn(mockTicket);

        TicketModel result = ticketService.createTicket(1L, "Sample Ticket", "Description");

        assertNotNull(result);
        assertEquals("Sample Ticket", result.getTitle());
        verify(ticketRepository, times(1)).save(any(TicketModel.class));
    }

    @Test
    void testUpdateTicket_TicketExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(mockTicket));
        when(ticketRepository.save(any(TicketModel.class))).thenReturn(mockTicket);

        TicketModel result = ticketService.updateTicket(1L, "CLOSED");

        assertNotNull(result);
        assertEquals("CLOSED", result.getStatus());
        verify(ticketRepository, times(1)).save(mockTicket);
    }

    @Test
    void testUpdateTicket_TicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.updateTicket(1L, "CLOSED"));
    }

    @Test
    void testGetTicketById_TicketExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(mockTicket));

        Optional<TicketModel> result = ticketService.getTicketById(1L);

        assertTrue(result.isPresent());
        assertEquals("Sample Ticket", result.get().getTitle());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById_TicketNotFound() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<TicketModel> result = ticketService.getTicketById(1L);

        assertFalse(result.isPresent());
    }

    @Test
    void testGetAllTickets() {
        Page<TicketModel> tickets = new PageImpl<>(List.of(mockTicket));
        when(ticketRepository.findAll(any(Pageable.class))).thenReturn(tickets);

        Page<TicketModel> result = ticketService.getAllTickets(Pageable.unpaged());

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(ticketRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void testDeleteTicket_TicketExists() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> ticketService.deleteTicket(1L));
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTicket_TicketNotFound() {
        when(ticketRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> ticketService.deleteTicket(1L));
    }

    @Test
    void testAddMessage() {
        when(messageRepository.save(any(MessageModel.class))).thenReturn(mockMessage);

        MessageModel result = ticketService.addMessage(1L, 1L, "Sample Message");

        assertNotNull(result);
        assertEquals("Sample Message", result.getMessage());
        verify(messageRepository, times(1)).save(any(MessageModel.class));
    }

    @Test
    void testGetMessagesByTicketId() {
        when(messageRepository.findByTicketId(1L)).thenReturn(List.of(mockMessage));

        List<MessageModel> result = ticketService.getMessagesByTicketId(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Sample Message", result.get(0).getMessage());
        verify(messageRepository, times(1)).findByTicketId(1L);
    }

    @Test
    void testGetUserById_UserExists() {
        UserModel mockUser = new UserModel();
        mockUser.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(ticketRepository.findByUserId(1L)).thenReturn(List.of(mockTicket));

        List<TicketModel> result = ticketService.getUserById("token", 1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(userRepository, times(1)).findById(1L);
        verify(ticketRepository, times(1)).findByUserId(1L);
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> ticketService.getUserById("token", 1L));
    }
}
