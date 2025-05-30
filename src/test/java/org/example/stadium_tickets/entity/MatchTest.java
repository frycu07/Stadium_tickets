package org.example.stadium_tickets.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MatchTest {

    @Test
    void testDefaultConstructor() {
        Match match = new Match();
        assertNull(match.getId());
        assertNull(match.getHomeTeam());
        assertNull(match.getAwayTeam());
        assertNull(match.getMatchDate());
        assertNull(match.getStadium());
        assertNotNull(match.getTickets());
        assertTrue(match.getTickets().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        Stadium stadium = new Stadium("Test Stadium", "Test City", 10000);
        LocalDateTime matchDate = LocalDateTime.now();
        Match match = new Match("Home Team", "Away Team", matchDate, stadium);

        assertNull(match.getId());
        assertEquals("Home Team", match.getHomeTeam());
        assertEquals("Away Team", match.getAwayTeam());
        assertEquals(matchDate, match.getMatchDate());
        assertEquals(stadium, match.getStadium());
        assertNotNull(match.getTickets());
        assertTrue(match.getTickets().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Match match = new Match();
        
        Long id = 1L;
        match.setId(id);
        assertEquals(id, match.getId());
        
        String homeTeam = "New Home Team";
        match.setHomeTeam(homeTeam);
        assertEquals(homeTeam, match.getHomeTeam());
        
        String awayTeam = "New Away Team";
        match.setAwayTeam(awayTeam);
        assertEquals(awayTeam, match.getAwayTeam());
        
        LocalDateTime matchDate = LocalDateTime.now();
        match.setMatchDate(matchDate);
        assertEquals(matchDate, match.getMatchDate());
        
        Stadium stadium = new Stadium("New Stadium", "New City", 20000);
        match.setStadium(stadium);
        assertEquals(stadium, match.getStadium());
        
        List<Ticket> tickets = new ArrayList<>();
        tickets.add(new Ticket());
        match.setTickets(tickets);
        assertEquals(tickets, match.getTickets());
        assertEquals(1, match.getTickets().size());
    }
}