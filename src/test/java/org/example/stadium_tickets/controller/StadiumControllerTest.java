package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.service.StadiumService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class StadiumControllerTest {

    private StadiumController stadiumController;
    private Stadium testStadium;

    @Mock
    private StadiumService stadiumService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stadiumController = new StadiumController(stadiumService);
        testStadium = new Stadium("Test Stadium", "Test City", 10000);

        // Setup mock behavior
        when(stadiumService.getAllStadiums()).thenReturn(new ArrayList<>());
        when(stadiumService.getStadiumById(anyLong())).thenReturn(testStadium);
        when(stadiumService.createStadium(any(Stadium.class))).thenReturn(testStadium);
        when(stadiumService.updateStadium(anyLong(), any(Stadium.class))).thenReturn(testStadium);
    }

    @Test
    void testGetAllStadiums() {
        // Initially the list should be empty
        ResponseEntity<List<Stadium>> response = stadiumController.getAllStadiums();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Add a stadium and check if it's returned
        List<Stadium> stadiumList = new ArrayList<>();
        stadiumList.add(testStadium);
        when(stadiumService.getAllStadiums()).thenReturn(stadiumList);

        response = stadiumController.getAllStadiums();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testStadium, response.getBody().get(0));
    }

    @Test
    void testGetStadiumById() {
        ResponseEntity<Stadium> response = stadiumController.getStadiumById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateStadium() {
        ResponseEntity<Stadium> response = stadiumController.createStadium(testStadium);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testStadium, response.getBody());

        // Verify the stadium was added to the list
        List<Stadium> stadiumList = new ArrayList<>();
        stadiumList.add(testStadium);
        when(stadiumService.getAllStadiums()).thenReturn(stadiumList);

        ResponseEntity<List<Stadium>> allStadiums = stadiumController.getAllStadiums();
        assertEquals(1, allStadiums.getBody().size());
        assertEquals(testStadium, allStadiums.getBody().get(0));
    }

    @Test
    void testUpdateStadium() {
        ResponseEntity<Stadium> response = stadiumController.updateStadium(1L, testStadium);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testStadium, response.getBody());
    }

    @Test
    void testDeleteStadium() {
        ResponseEntity<Void> response = stadiumController.deleteStadium(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
