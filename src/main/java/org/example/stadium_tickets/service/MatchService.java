package org.example.stadium_tickets.service;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;

import java.time.LocalDateTime;
import java.util.List;

public interface MatchService {
    List<Match> getAllMatches();
    Match getMatchById(Long id);
    Match createMatch(Match match);
    Match updateMatch(Long id, Match match);
    void deleteMatch(Long id);
    List<Match> findByHomeTeam(String homeTeam);
    List<Match> findByAwayTeam(String awayTeam);
    List<Match> findByStadium(Stadium stadium);
    List<Match> findByMatchDateBetween(LocalDateTime start, LocalDateTime end);
    List<Match> findByHomeTeamAndAwayTeam(String homeTeam, String awayTeam);
}