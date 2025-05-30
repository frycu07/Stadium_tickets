package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.entity.Ticket;
import org.example.stadium_tickets.service.MatchService;
import org.example.stadium_tickets.service.TicketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class TicketControllerTest {

    private TicketController ticketController;
    private Ticket testTicket;
    private Match testMatch;
    private Stadium testStadium;

    @Mock
    private TicketService ticketService;

    @Mock
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ticketController = new TicketController(ticketService, matchService);
        testStadium = new Stadium("Test Stadium", "Test City", 10000);
        testMatch = new Match("Home Team", "Away Team", LocalDateTime.now(), testStadium);
        testTicket = new Ticket(testMatch, "A", "12", new BigDecimal("50.00"));

        // Setup mock behavior
        when(ticketService.getAllTickets()).thenReturn(new ArrayList<>());
        when(ticketService.getTicketById(anyLong())).thenReturn(testTicket);
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);
        when(matchService.getMatchById(anyLong())).thenReturn(testMatch);
        when(ticketService.findByMatch(any(Match.class))).thenReturn(new ArrayList<>());
    }

    @Test
    void testGetAllTickets() {
        // Initially the list should be empty
        when(ticketService.getAllTickets()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Ticket>> response = ticketController.getAllTickets();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Add a ticket and check if it's returned
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(testTicket);
        when(ticketService.getAllTickets()).thenReturn(ticketList);
        response = ticketController.getAllTickets();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testTicket, response.getBody().get(0));
    }

    @Test
    void testGetTicketById() {
        ResponseEntity<Ticket> response = ticketController.getTicketById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetTicketsByMatchId() {
        ResponseEntity<List<Ticket>> response = ticketController.getTicketsByMatchId(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
    }


    @Test
    void testPurchaseTicket() {
        when(ticketService.createTicket(any(Ticket.class))).thenReturn(testTicket);
        ResponseEntity<Ticket> response = ticketController.purchaseTicket(testTicket);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testTicket, response.getBody());

        // Verify the ticket was added to the list
        List<Ticket> ticketList = new ArrayList<>();
        ticketList.add(testTicket);
        when(ticketService.getAllTickets()).thenReturn(ticketList);
        ResponseEntity<List<Ticket>> allTickets = ticketController.getAllTickets();
        assertEquals(1, allTickets.getBody().size());
        assertEquals(testTicket, allTickets.getBody().get(0));
    }

    @Test
    void testCancelTicket() {
        ResponseEntity<Void> response = ticketController.cancelTicket(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
