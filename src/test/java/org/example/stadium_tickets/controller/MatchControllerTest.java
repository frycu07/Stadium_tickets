package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {

    private MatchController matchController;
    private Match testMatch;
    private Stadium testStadium;

    @Mock
    private MatchService matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        matchController = new MatchController(matchService);
        testStadium = new Stadium("Test Stadium", "Test City", 10000);
        testMatch = new Match("Home Team", "Away Team", LocalDateTime.now(), testStadium);

        // Setup mock behavior
        when(matchService.getAllMatches()).thenReturn(new ArrayList<>());
        when(matchService.getMatchById(anyLong())).thenReturn(testMatch);
        when(matchService.createMatch(any(Match.class))).thenReturn(testMatch);
        when(matchService.updateMatch(anyLong(), any(Match.class))).thenReturn(testMatch);
    }

    @Test
    void testGetAllMatches() {
        // Initially the list should be empty
        when(matchService.getAllMatches()).thenReturn(new ArrayList<>());
        ResponseEntity<List<Match>> response = matchController.getAllMatches();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Add a match and check if it's returned
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        when(matchService.getAllMatches()).thenReturn(matchList);
        response = matchController.getAllMatches();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testMatch, response.getBody().get(0));
    }

    @Test
    void testGetMatchById() {
        ResponseEntity<Match> response = matchController.getMatchById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateMatch() {
        when(matchService.createMatch(any(Match.class))).thenReturn(testMatch);
        ResponseEntity<Match> response = matchController.createMatch(testMatch);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMatch, response.getBody());

        // Verify the match was added to the list
        List<Match> matchList = new ArrayList<>();
        matchList.add(testMatch);
        when(matchService.getAllMatches()).thenReturn(matchList);
        ResponseEntity<List<Match>> allMatches = matchController.getAllMatches();
        assertEquals(1, allMatches.getBody().size());
        assertEquals(testMatch, allMatches.getBody().get(0));
    }

    @Test
    void testUpdateMatch() {
        ResponseEntity<Match> response = matchController.updateMatch(1L, testMatch);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMatch, response.getBody());
    }

    @Test
    void testDeleteMatch() {
        ResponseEntity<Void> response = matchController.deleteMatch(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
