package org.example.stadium_tickets.repository;

import org.example.stadium_tickets.entity.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
    Optional<Stadium> findByName(String name);
    List<Stadium> findByCity(String city);
    boolean existsByName(String name);
}