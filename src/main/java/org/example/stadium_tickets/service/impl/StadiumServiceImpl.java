package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.repository.StadiumRepository;
import org.example.stadium_tickets.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StadiumServiceImpl implements StadiumService {

    private final StadiumRepository stadiumRepository;

    @Autowired
    public StadiumServiceImpl(StadiumRepository stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    @Override
    public List<Stadium> getAllStadiums() {
        return stadiumRepository.findAll();
    }

    @Override
    public Stadium getStadiumById(Long id) {
        return stadiumRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Stadium not found with id: " + id));
    }

    @Override
    @Transactional
    public Stadium createStadium(Stadium stadium) {
        if (existsByName(stadium.getName())) {
            throw new RuntimeException("Stadium already exists with name: " + stadium.getName());
        }
        return stadiumRepository.save(stadium);
    }

    @Override
    @Transactional
    public Stadium updateStadium(Long id, Stadium stadium) {
        Stadium existingStadium = getStadiumById(id);
        
        // Check if name is being changed and if it already exists
        if (!existingStadium.getName().equals(stadium.getName()) && existsByName(stadium.getName())) {
            throw new RuntimeException("Stadium already exists with name: " + stadium.getName());
        }
        
        // Update fields
        existingStadium.setName(stadium.getName());
        existingStadium.setCity(stadium.getCity());
        existingStadium.setCapacity(stadium.getCapacity());
        
        return stadiumRepository.save(existingStadium);
    }

    @Override
    @Transactional
    public void deleteStadium(Long id) {
        if (!stadiumRepository.existsById(id)) {
            throw new RuntimeException("Stadium not found with id: " + id);
        }
        stadiumRepository.deleteById(id);
    }

    @Override
    public Optional<Stadium> findByName(String name) {
        return stadiumRepository.findByName(name);
    }

    @Override
    public List<Stadium> findByCity(String city) {
        return stadiumRepository.findByCity(city);
    }

    @Override
    public boolean existsByName(String name) {
        return stadiumRepository.existsByName(name);
    }
}