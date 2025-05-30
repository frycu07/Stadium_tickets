package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.entity.Stadium;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchControllerTest {

    private MatchController matchController;
    private Match testMatch;
    private Stadium testStadium;

    @BeforeEach
    void setUp() {
        matchController = new MatchController();
        testStadium = new Stadium("Test Stadium", "Test City", 10000);
        testMatch = new Match("Home Team", "Away Team", LocalDateTime.now(), testStadium);
    }

    @Test
    void testGetAllMatches() {
        // Initially the list should be empty
        ResponseEntity<List<Match>> response = matchController.getAllMatches();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Add a match and check if it's returned
        matchController.createMatch(testMatch);
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
        ResponseEntity<Match> response = matchController.createMatch(testMatch);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testMatch, response.getBody());
        
        // Verify the match was added to the list
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