package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.repository.MatchRepository;
import org.example.stadium_tickets.service.MatchService;
import org.example.stadium_tickets.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final StadiumService stadiumService;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, StadiumService stadiumService) {
        this.matchRepository = matchRepository;
        this.stadiumService = stadiumService;
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(Long id) {
        return matchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Match not found with id: " + id));
    }

    @Override
    @Transactional
    public Match createMatch(Match match) {
        // Validate stadium exists
        if (match.getStadium() != null && match.getStadium().getId() != null) {
            Stadium stadium = stadiumService.getStadiumById(match.getStadium().getId());
            match.setStadium(stadium);
        } else {
            throw new RuntimeException("Stadium is required for a match");
        }
        
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public Match updateMatch(Long id, Match match) {
        Match existingMatch = getMatchById(id);
        
        // Validate stadium exists if it's being updated
        if (match.getStadium() != null && match.getStadium().getId() != null) {
            Stadium stadium = stadiumService.getStadiumById(match.getStadium().getId());
            existingMatch.setStadium(stadium);
        }
        
        // Update fields
        existingMatch.setHomeTeam(match.getHomeTeam());
        existingMatch.setAwayTeam(match.getAwayTeam());
        existingMatch.setMatchDate(match.getMatchDate());
        
        return matchRepository.save(existingMatch);
    }

    @Override
    @Transactional
    public void deleteMatch(Long id) {
        if (!matchRepository.existsById(id)) {
            throw new RuntimeException("Match not found with id: " + id);
        }
        matchRepository.deleteById(id);
    }

    @Override
    public List<Match> findByHomeTeam(String homeTeam) {
        return matchRepository.findByHomeTeam(homeTeam);
    }

    @Override
    public List<Match> findByAwayTeam(String awayTeam) {
        return matchRepository.findByAwayTeam(awayTeam);
    }

    @Override
    public List<Match> findByStadium(Stadium stadium) {
        return matchRepository.findByStadium(stadium);
    }

    @Override
    public List<Match> findByMatchDateBetween(LocalDateTime start, LocalDateTime end) {
        return matchRepository.findByMatchDateBetween(start, end);
    }

    @Override
    public List<Match> findByHomeTeamAndAwayTeam(String homeTeam, String awayTeam) {
        return matchRepository.findByHomeTeamAndAwayTeam(homeTeam, awayTeam);
    }
}