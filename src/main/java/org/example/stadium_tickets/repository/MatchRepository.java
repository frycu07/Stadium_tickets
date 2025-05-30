package org.example.stadium_tickets.repository;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MatchRepository extends JpaRepository<Match, Long> {
    List<Match> findByHomeTeam(String homeTeam);
    List<Match> findByAwayTeam(String awayTeam);
    List<Match> findByStadium(Stadium stadium);
    List<Match> findByMatchDateBetween(LocalDateTime start, LocalDateTime end);
    List<Match> findByHomeTeamAndAwayTeam(String homeTeam, String awayTeam);
}