package org.example.stadium_tickets.entity;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StadiumTest {

    @Test
    void testDefaultConstructor() {
        Stadium stadium = new Stadium();
        assertNull(stadium.getId());
        assertNull(stadium.getName());
        assertNull(stadium.getCity());
        assertNull(stadium.getCapacity());
        assertNotNull(stadium.getMatches());
        assertTrue(stadium.getMatches().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "Test Stadium";
        String city = "Test City";
        Integer capacity = 10000;
        
        Stadium stadium = new Stadium(name, city, capacity);
        
        assertNull(stadium.getId());
        assertEquals(name, stadium.getName());
        assertEquals(city, stadium.getCity());
        assertEquals(capacity, stadium.getCapacity());
        assertNotNull(stadium.getMatches());
        assertTrue(stadium.getMatches().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Stadium stadium = new Stadium();
        
        Long id = 1L;
        stadium.setId(id);
        assertEquals(id, stadium.getId());
        
        String name = "New Stadium";
        stadium.setName(name);
        assertEquals(name, stadium.getName());
        
        String city = "New City";
        stadium.setCity(city);
        assertEquals(city, stadium.getCity());
        
        Integer capacity = 20000;
        stadium.setCapacity(capacity);
        assertEquals(capacity, stadium.getCapacity());
        
        List<Match> matches = new ArrayList<>();
        Match match = new Match();
        matches.add(match);
        stadium.setMatches(matches);
        assertEquals(matches, stadium.getMatches());
        assertEquals(1, stadium.getMatches().size());
    }
}