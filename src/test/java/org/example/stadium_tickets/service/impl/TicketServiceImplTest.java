package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.entity.Ticket;
import org.example.stadium_tickets.repository.TicketRepository;
import org.example.stadium_tickets.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketServiceImplTest {

    private TicketServiceImpl ticketService;
    private Ticket testTicket;
    private Match testMatch;
    private Stadium testStadium;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketService = new TicketServiceImpl(ticketRepository, matchService);
        
        testStadium = new Stadium("Test Stadium", "Test City", 10000);
        testStadium.setId(1L);
        
        testMatch = new Match("Home Team", "Away Team", LocalDateTime.now(), testStadium);
        testMatch.setId(1L);
        
        testTicket = new Ticket(testMatch, "A", "1", BigDecimal.valueOf(100.0));
        testTicket.setId(1L);
        testTicket.setStatus("FREE");
        
        // Setup mock behavior
        when(ticketRepository.findAll()).thenReturn(new ArrayList<>());
        when(ticketRepository.findById(anyLong())).thenReturn(Optional.of(testTicket));
        when(ticketRepository.save(any(Ticket.class))).thenReturn(testTicket);
        when(matchService.getMatchById(anyLong())).thenReturn(testMatch);
    }

    @Test
    void testGetAllTickets() {
        // Initially the list should be empty
        List<Ticket> tickets = ticketService.getAllTickets();
        assertNotNull(tickets);
        assertTrue(tickets.isEmpty());

        // Add a ticket and check if it's returned
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(testTicket);
        when(ticketRepository.findAll()).thenReturn(ticketList);
        
        tickets = ticketService.getAllTickets();
        assertEquals(1, tickets.size());
        assertEquals(testTicket, tickets.get(0));
    }

    @Test
    void testGetTicketById() {
        Ticket ticket = ticketService.getTicketById(1L);
        assertNotNull(ticket);
        assertEquals(testTicket, ticket);
    }

    @Test
    void testGetTicketByIdNotFound() {
        when(ticketRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.getTicketById(99L);
        });
        
        assertTrue(exception.getMessage().contains("Ticket not found"));
    }

    @Test
    void testCreateTicket() {
        Ticket newTicket = new Ticket(testMatch, "B", "2", BigDecimal.valueOf(150.0));
        when(ticketRepository.findByMatchAndSeatRowAndSeatNumber(any(Match.class), anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(newTicket);
        
        Ticket createdTicket = ticketService.createTicket(newTicket);
        assertNotNull(createdTicket);
        assertEquals(newTicket, createdTicket);
        assertEquals("FREE", createdTicket.getStatus());
        
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testCreateTicketWithoutMatch() {
        Ticket newTicket = new Ticket(null, "B", "2", BigDecimal.valueOf(150.0));
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.createTicket(newTicket);
        });
        
        assertTrue(exception.getMessage().contains("Match is required"));
    }

    @Test
    void testCreateTicketWithExistingSeat() {
        Ticket newTicket = new Ticket(testMatch, "A", "1", BigDecimal.valueOf(150.0));
        when(ticketRepository.findByMatchAndSeatRowAndSeatNumber(any(Match.class), eq("A"), eq("1")))
            .thenReturn(Optional.of(testTicket));
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.createTicket(newTicket);
        });
        
        assertTrue(exception.getMessage().contains("Seat already exists"));
    }

    @Test
    void testUpdateTicket() {
        Ticket updatedTicket = new Ticket(testMatch, "C", "3", BigDecimal.valueOf(200.0));
        updatedTicket.setId(1L);
        updatedTicket.setStatus("SOLD");
        
        when(ticketRepository.findByMatchAndSeatRowAndSeatNumber(any(Match.class), anyString(), anyString()))
            .thenReturn(Optional.empty());
        when(ticketRepository.save(any(Ticket.class))).thenReturn(updatedTicket);
        
        Ticket result = ticketService.updateTicket(1L, updatedTicket);
        assertNotNull(result);
        assertEquals(updatedTicket, result);
        
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testUpdateTicketWithExistingSeat() {
        Ticket existingTicket = new Ticket(testMatch, "D", "4", BigDecimal.valueOf(250.0));
        existingTicket.setId(2L);
        
        Ticket updatedTicket = new Ticket(testMatch, "D", "4", BigDecimal.valueOf(200.0));
        updatedTicket.setId(1L);
        
        when(ticketRepository.findByMatchAndSeatRowAndSeatNumber(any(Match.class), eq("D"), eq("4")))
            .thenReturn(Optional.of(existingTicket));
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.updateTicket(1L, updatedTicket);
        });
        
        assertTrue(exception.getMessage().contains("Seat already exists"));
    }

    @Test
    void testDeleteTicket() {
        when(ticketRepository.existsById(1L)).thenReturn(true);
        
        ticketService.deleteTicket(1L);
        
        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTicketNotFound() {
        when(ticketRepository.existsById(99L)).thenReturn(false);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.deleteTicket(99L);
        });
        
        assertTrue(exception.getMessage().contains("Ticket not found"));
    }

    @Test
    void testFindByMatch() {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(testTicket);
        
        when(ticketRepository.findByMatch(testMatch)).thenReturn(ticketList);
        
        List<Ticket> result = ticketService.findByMatch(testMatch);
        assertEquals(1, result.size());
        assertEquals(testTicket, result.get(0));
    }

    @Test
    void testFindByStatus() {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(testTicket);
        
        when(ticketRepository.findByStatus("FREE")).thenReturn(ticketList);
        
        List<Ticket> result = ticketService.findByStatus("FREE");
        assertEquals(1, result.size());
        assertEquals(testTicket, result.get(0));
    }

    @Test
    void testFindByMatchAndStatus() {
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(testTicket);
        
        when(ticketRepository.findByMatchAndStatus(testMatch, "FREE")).thenReturn(ticketList);
        
        List<Ticket> result = ticketService.findByMatchAndStatus(testMatch, "FREE");
        assertEquals(1, result.size());
        assertEquals(testTicket, result.get(0));
    }

    @Test
    void testFindByMatchAndSeatRowAndSeatNumber() {
        when(ticketRepository.findByMatchAndSeatRowAndSeatNumber(testMatch, "A", "1"))
            .thenReturn(Optional.of(testTicket));
        
        Optional<Ticket> result = ticketService.findByMatchAndSeatRowAndSeatNumber(testMatch, "A", "1");
        assertTrue(result.isPresent());
        assertEquals(testTicket, result.get());
    }

    @Test
    void testCountByMatchAndStatus() {
        when(ticketRepository.countByMatchAndStatus(testMatch, "FREE")).thenReturn(5L);
        
        long count = ticketService.countByMatchAndStatus(testMatch, "FREE");
        assertEquals(5L, count);
    }

    @Test
    void testPurchaseTicket() {
        Ticket soldTicket = new Ticket(testMatch, "A", "1", BigDecimal.valueOf(100.0));
        soldTicket.setId(1L);
        soldTicket.setStatus("SOLD");
        
        when(ticketRepository.save(any(Ticket.class))).thenReturn(soldTicket);
        
        Ticket result = ticketService.purchaseTicket(1L);
        assertNotNull(result);
        assertEquals("SOLD", result.getStatus());
        
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testPurchaseTicketNotAvailable() {
        testTicket.setStatus("SOLD");
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.purchaseTicket(1L);
        });
        
        assertTrue(exception.getMessage().contains("not available for purchase"));
    }

    @Test
    void testCancelTicket() {
        testTicket.setStatus("SOLD");
        
        Ticket canceledTicket = new Ticket(testMatch, "A", "1", BigDecimal.valueOf(100.0));
        canceledTicket.setId(1L);
        canceledTicket.setStatus("FREE");
        
        when(ticketRepository.save(any(Ticket.class))).thenReturn(canceledTicket);
        
        Ticket result = ticketService.cancelTicket(1L);
        assertNotNull(result);
        assertEquals("FREE", result.getStatus());
        
        verify(ticketRepository, times(1)).save(any(Ticket.class));
    }

    @Test
    void testCancelTicketNotSold() {
        Exception exception = assertThrows(RuntimeException.class, () -> {
            ticketService.cancelTicket(1L);
        });
        
        assertTrue(exception.getMessage().contains("not sold, cannot be canceled"));
    }
}