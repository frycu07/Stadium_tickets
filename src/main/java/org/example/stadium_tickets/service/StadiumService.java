package org.example.stadium_tickets.service;

import org.example.stadium_tickets.entity.Stadium;

import java.util.List;
import java.util.Optional;

public interface StadiumService {
    List<Stadium> getAllStadiums();
    Stadium getStadiumById(Long id);
    Stadium createStadium(Stadium stadium);
    Stadium updateStadium(Long id, Stadium stadium);
    void deleteStadium(Long id);
    Optional<Stadium> findByName(String name);
    List<Stadium> findByCity(String city);
    boolean existsByName(String name);
}