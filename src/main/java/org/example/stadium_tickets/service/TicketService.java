package org.example.stadium_tickets.service;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    List<Ticket> getAllTickets();
    Ticket getTicketById(Long id);
    Ticket createTicket(Ticket ticket);
    Ticket updateTicket(Long id, Ticket ticket);
    void deleteTicket(Long id);
    List<Ticket> findByMatch(Match match);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByMatchAndStatus(Match match, String status);
    Optional<Ticket> findByMatchAndSeatRowAndSeatNumber(Match match, String seatRow, String seatNumber);
    long countByMatchAndStatus(Match match, String status);
    Ticket purchaseTicket(Long ticketId);
    Ticket cancelTicket(Long ticketId);
}