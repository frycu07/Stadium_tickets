package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.repository.StadiumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StadiumServiceImplTest {

    private StadiumServiceImpl stadiumService;
    private Stadium testStadium;

    @Mock
    private StadiumRepository stadiumRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        stadiumService = new StadiumServiceImpl(stadiumRepository);
        
        testStadium = new Stadium("Test Stadium", "Test City", 10000);
        testStadium.setId(1L);
        
        // Setup mock behavior
        when(stadiumRepository.findAll()).thenReturn(new ArrayList<>());
        when(stadiumRepository.findById(anyLong())).thenReturn(Optional.of(testStadium));
        when(stadiumRepository.save(any(Stadium.class))).thenReturn(testStadium);
    }

    @Test
    void testGetAllStadiums() {
        // Initially the list should be empty
        List<Stadium> stadiums = stadiumService.getAllStadiums();
        assertNotNull(stadiums);
        assertTrue(stadiums.isEmpty());

        // Add a stadium and check if it's returned
        List<Stadium> stadiumList = new ArrayList<>();
        stadiumList.add(testStadium);
        when(stadiumRepository.findAll()).thenReturn(stadiumList);
        
        stadiums = stadiumService.getAllStadiums();
        assertEquals(1, stadiums.size());
        assertEquals(testStadium, stadiums.get(0));
    }

    @Test
    void testGetStadiumById() {
        Stadium stadium = stadiumService.getStadiumById(1L);
        assertNotNull(stadium);
        assertEquals(testStadium, stadium);
    }

    @Test
    void testGetStadiumByIdNotFound() {
        when(stadiumRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            stadiumService.getStadiumById(99L);
        });
        
        assertTrue(exception.getMessage().contains("Stadium not found"));
    }

    @Test
    void testCreateStadium() {
        Stadium newStadium = new Stadium("New Stadium", "New City", 20000);
        when(stadiumRepository.existsByName("New Stadium")).thenReturn(false);
        when(stadiumRepository.save(any(Stadium.class))).thenReturn(newStadium);
        
        Stadium createdStadium = stadiumService.createStadium(newStadium);
        assertNotNull(createdStadium);
        assertEquals(newStadium, createdStadium);
        
        verify(stadiumRepository, times(1)).save(any(Stadium.class));
    }

    @Test
    void testCreateStadiumWithExistingName() {
        Stadium newStadium = new Stadium("Existing Stadium", "New City", 20000);
        when(stadiumRepository.existsByName("Existing Stadium")).thenReturn(true);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            stadiumService.createStadium(newStadium);
        });
        
        assertTrue(exception.getMessage().contains("Stadium already exists with name"));
    }

    @Test
    void testUpdateStadium() {
        Stadium updatedStadium = new Stadium("Updated Stadium", "Updated City", 30000);
        updatedStadium.setId(1L);
        
        when(stadiumRepository.existsByName("Updated Stadium")).thenReturn(false);
        when(stadiumRepository.save(any(Stadium.class))).thenReturn(updatedStadium);
        
        Stadium result = stadiumService.updateStadium(1L, updatedStadium);
        assertNotNull(result);
        assertEquals(updatedStadium, result);
        
        verify(stadiumRepository, times(1)).save(any(Stadium.class));
    }

    @Test
    void testUpdateStadiumWithExistingName() {
        Stadium updatedStadium = new Stadium("Existing Stadium", "Updated City", 30000);
        updatedStadium.setId(1L);
        
        when(stadiumRepository.existsByName("Existing Stadium")).thenReturn(true);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            stadiumService.updateStadium(1L, updatedStadium);
        });
        
        assertTrue(exception.getMessage().contains("Stadium already exists with name"));
    }

    @Test
    void testDeleteStadium() {
        when(stadiumRepository.existsById(1L)).thenReturn(true);
        
        stadiumService.deleteStadium(1L);
        
        verify(stadiumRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteStadiumNotFound() {
        when(stadiumRepository.existsById(99L)).thenReturn(false);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            stadiumService.deleteStadium(99L);
        });
        
        assertTrue(exception.getMessage().contains("Stadium not found"));
    }

    @Test
    void testFindByName() {
        when(stadiumRepository.findByName("Test Stadium")).thenReturn(Optional.of(testStadium));
        
        Optional<Stadium> result = stadiumService.findByName("Test Stadium");
        assertTrue(result.isPresent());
        assertEquals(testStadium, result.get());
    }

    @Test
    void testFindByCity() {
        List<Stadium> stadiumList = new ArrayList<>();
        stadiumList.add(testStadium);
        
        when(stadiumRepository.findByCity("Test City")).thenReturn(stadiumList);
        
        List<Stadium> result = stadiumService.findByCity("Test City");
        assertEquals(1, result.size());
        assertEquals(testStadium, result.get(0));
    }

    @Test
    void testExistsByName() {
        when(stadiumRepository.existsByName("Test Stadium")).thenReturn(true);
        when(stadiumRepository.existsByName("Non-existent Stadium")).thenReturn(false);
        
        assertTrue(stadiumService.existsByName("Test Stadium"));
        assertFalse(stadiumService.existsByName("Non-existent Stadium"));
    }
}