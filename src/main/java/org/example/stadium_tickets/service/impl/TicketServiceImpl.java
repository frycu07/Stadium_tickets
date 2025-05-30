package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Ticket;
import org.example.stadium_tickets.repository.TicketRepository;
import org.example.stadium_tickets.service.MatchService;
import org.example.stadium_tickets.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final MatchService matchService;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository, MatchService matchService) {
        this.ticketRepository = ticketRepository;
        this.matchService = matchService;
    }

    @Override
    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll();
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found with id: " + id));
    }

    @Override
    @Transactional
    public Ticket createTicket(Ticket ticket) {
        // Validate match exists
        if (ticket.getMatch() != null && ticket.getMatch().getId() != null) {
            Match match = matchService.getMatchById(ticket.getMatch().getId());
            ticket.setMatch(match);
        } else {
            throw new RuntimeException("Match is required for a ticket");
        }
        
        // Check if seat is already taken
        Optional<Ticket> existingTicket = findByMatchAndSeatRowAndSeatNumber(
                ticket.getMatch(), ticket.getSeatRow(), ticket.getSeatNumber());
        if (existingTicket.isPresent()) {
            throw new RuntimeException("Seat already exists for this match");
        }
        
        // Set initial status
        ticket.setStatus("FREE");
        
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket updateTicket(Long id, Ticket ticket) {
        Ticket existingTicket = getTicketById(id);
        
        // Validate match exists if it's being updated
        if (ticket.getMatch() != null && ticket.getMatch().getId() != null) {
            Match match = matchService.getMatchById(ticket.getMatch().getId());
            existingTicket.setMatch(match);
        }
        
        // Check if seat is being changed and if it's already taken
        if ((!existingTicket.getSeatRow().equals(ticket.getSeatRow()) || 
             !existingTicket.getSeatNumber().equals(ticket.getSeatNumber())) &&
            findByMatchAndSeatRowAndSeatNumber(
                existingTicket.getMatch(), ticket.getSeatRow(), ticket.getSeatNumber()).isPresent()) {
            throw new RuntimeException("Seat already exists for this match");
        }
        
        // Update fields
        existingTicket.setSeatRow(ticket.getSeatRow());
        existingTicket.setSeatNumber(ticket.getSeatNumber());
        existingTicket.setPrice(ticket.getPrice());
        existingTicket.setStatus(ticket.getStatus());
        
        return ticketRepository.save(existingTicket);
    }

    @Override
    @Transactional
    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new RuntimeException("Ticket not found with id: " + id);
        }
        ticketRepository.deleteById(id);
    }

    @Override
    public List<Ticket> findByMatch(Match match) {
        return ticketRepository.findByMatch(match);
    }

    @Override
    public List<Ticket> findByStatus(String status) {
        return ticketRepository.findByStatus(status);
    }

    @Override
    public List<Ticket> findByMatchAndStatus(Match match, String status) {
        return ticketRepository.findByMatchAndStatus(match, status);
    }

    @Override
    public Optional<Ticket> findByMatchAndSeatRowAndSeatNumber(Match match, String seatRow, String seatNumber) {
        return ticketRepository.findByMatchAndSeatRowAndSeatNumber(match, seatRow, seatNumber);
    }

    @Override
    public long countByMatchAndStatus(Match match, String status) {
        return ticketRepository.countByMatchAndStatus(match, status);
    }

    @Override
    @Transactional
    public Ticket purchaseTicket(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        
        if (!"FREE".equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket is not available for purchase");
        }
        
        ticket.setStatus("SOLD");
        return ticketRepository.save(ticket);
    }

    @Override
    @Transactional
    public Ticket cancelTicket(Long ticketId) {
        Ticket ticket = getTicketById(ticketId);
        
        if (!"SOLD".equals(ticket.getStatus())) {
            throw new RuntimeException("Ticket is not sold, cannot be canceled");
        }
        
        ticket.setStatus("FREE");
        return ticketRepository.save(ticket);
    }
}