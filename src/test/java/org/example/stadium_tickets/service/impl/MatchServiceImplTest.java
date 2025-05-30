package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.repository.MatchRepository;
import org.example.stadium_tickets.service.StadiumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MatchServiceImplTest {

    private MatchServiceImpl matchService;
    private Match testMatch;
    private Stadium testStadium;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private StadiumService stadiumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        matchService = new MatchServiceImpl(matchRepository, stadiumService);
        
        testStadium = new Stadium("Test Stadium", "Test City", 10000);
        testStadium.setId(1L);
        
        testMatch = new Match("Home Team", "Away Team", LocalDateTime.now(), testStadium);
        testMatch.setId(1L);
        
        // Setup mock behavior
        when(matchRepository.findAll()).thenReturn(new ArrayList<>());
        when(matchRepository.findById(anyLong())).thenReturn(Optional.of(testMatch));
        when(matchRepository.save(any(Match.class))).thenReturn(testMatch);
        when(stadiumService.getStadiumById(anyLong())).thenReturn(testStadium);
    }

    @Test
    void testGetAllMatches() {
        // Initially the list should be empty
        List<Match> matches = matchService.getAllMatches();
        assertNotNull(matches);
        assertTrue(matches.isEmpty());

        // Add a match and check if it's returned
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        when(matchRepository.findAll()).thenReturn(matchList);
        
        matches = matchService.getAllMatches();
        assertEquals(1, matches.size());
        assertEquals(testMatch, matches.get(0));
    }

    @Test
    void testGetMatchById() {
        Match match = matchService.getMatchById(1L);
        assertNotNull(match);
        assertEquals(testMatch, match);
    }

    @Test
    void testGetMatchByIdNotFound() {
        when(matchRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            matchService.getMatchById(99L);
        });
        
        assertTrue(exception.getMessage().contains("Match not found"));
    }

    @Test
    void testCreateMatch() {
        Match newMatch = new Match("New Home", "New Away", LocalDateTime.now(), testStadium);
        when(matchRepository.save(any(Match.class))).thenReturn(newMatch);
        
        Match createdMatch = matchService.createMatch(newMatch);
        assertNotNull(createdMatch);
        assertEquals(newMatch, createdMatch);
        
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void testCreateMatchWithoutStadium() {
        Match newMatch = new Match("New Home", "New Away", LocalDateTime.now(), null);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            matchService.createMatch(newMatch);
        });
        
        assertTrue(exception.getMessage().contains("Stadium is required"));
    }

    @Test
    void testUpdateMatch() {
        Match updatedMatch = new Match("Updated Home", "Updated Away", LocalDateTime.now(), testStadium);
        updatedMatch.setId(1L);
        
        when(matchRepository.save(any(Match.class))).thenReturn(updatedMatch);
        
        Match result = matchService.updateMatch(1L, updatedMatch);
        assertNotNull(result);
        assertEquals(updatedMatch, result);
        
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    void testDeleteMatch() {
        when(matchRepository.existsById(1L)).thenReturn(true);
        
        matchService.deleteMatch(1L);
        
        verify(matchRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteMatchNotFound() {
        when(matchRepository.existsById(99L)).thenReturn(false);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            matchService.deleteMatch(99L);
        });
        
        assertTrue(exception.getMessage().contains("Match not found"));
    }

    @Test
    void testFindByHomeTeam() {
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        
        when(matchRepository.findByHomeTeam("Home Team")).thenReturn(matchList);
        
        List<Match> result = matchService.findByHomeTeam("Home Team");
        assertEquals(1, result.size());
        assertEquals(testMatch, result.get(0));
    }

    @Test
    void testFindByAwayTeam() {
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        
        when(matchRepository.findByAwayTeam("Away Team")).thenReturn(matchList);
        
        List<Match> result = matchService.findByAwayTeam("Away Team");
        assertEquals(1, result.size());
        assertEquals(testMatch, result.get(0));
    }

    @Test
    void testFindByStadium() {
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        
        when(matchRepository.findByStadium(testStadium)).thenReturn(matchList);
        
        List<Match> result = matchService.findByStadium(testStadium);
        assertEquals(1, result.size());
        assertEquals(testMatch, result.get(0));
    }

    @Test
    void testFindByMatchDateBetween() {
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        
        LocalDateTime start = LocalDateTime.now().minusDays(1);
        LocalDateTime end = LocalDateTime.now().plusDays(1);
        
        when(matchRepository.findByMatchDateBetween(start, end)).thenReturn(matchList);
        
        List<Match> result = matchService.findByMatchDateBetween(start, end);
        assertEquals(1, result.size());
        assertEquals(testMatch, result.get(0));
    }

    @Test
    void testFindByHomeTeamAndAwayTeam() {
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        
        when(matchRepository.findByHomeTeamAndAwayTeam("Home Team", "Away Team")).thenReturn(matchList);
        
        List<Match> result = matchService.findByHomeTeamAndAwayTeam("Home Team", "Away Team");
        assertEquals(1, result.size());
        assertEquals(testMatch, result.get(0));
    }
}