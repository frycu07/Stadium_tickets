package org.example.stadium_tickets.entity;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TicketTest {

    @Test
    void testDefaultConstructor() {
        Ticket ticket = new Ticket();
        assertNull(ticket.getId());
        assertNull(ticket.getMatch());
        assertNull(ticket.getSeatRow());
        assertNull(ticket.getSeatNumber());
        assertNull(ticket.getPrice());
        assertEquals("FREE", ticket.getStatus());
    }

    @Test
    void testParameterizedConstructor() {
        Match match = new Match();
        String seatRow = "A";
        String seatNumber = "12";
        BigDecimal price = new BigDecimal("50.00");
        
        Ticket ticket = new Ticket(match, seatRow, seatNumber, price);
        
        assertNull(ticket.getId());
        assertEquals(match, ticket.getMatch());
        assertEquals(seatRow, ticket.getSeatRow());
        assertEquals(seatNumber, ticket.getSeatNumber());
        assertEquals(price, ticket.getPrice());
        assertEquals("FREE", ticket.getStatus());
    }

    @Test
    void testSettersAndGetters() {
        Ticket ticket = new Ticket();
        
        Long id = 1L;
        ticket.setId(id);
        assertEquals(id, ticket.getId());
        
        Match match = new Match();
        ticket.setMatch(match);
        assertEquals(match, ticket.getMatch());
        
        String seatRow = "B";
        ticket.setSeatRow(seatRow);
        assertEquals(seatRow, ticket.getSeatRow());
        
        String seatNumber = "15";
        ticket.setSeatNumber(seatNumber);
        assertEquals(seatNumber, ticket.getSeatNumber());
        
        BigDecimal price = new BigDecimal("75.50");
        ticket.setPrice(price);
        assertEquals(price, ticket.getPrice());
        
        String status = "SOLD";
        ticket.setStatus(status);
        assertEquals(status, ticket.getStatus());
    }
}