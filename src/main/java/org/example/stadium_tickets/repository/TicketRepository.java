package org.example.stadium_tickets.repository;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByMatch(Match match);
    List<Ticket> findByStatus(String status);
    List<Ticket> findByMatchAndStatus(Match match, String status);
    Optional<Ticket> findByMatchAndSeatRowAndSeatNumber(Match match, String seatRow, String seatNumber);
    long countByMatchAndStatus(Match match, String status);
}