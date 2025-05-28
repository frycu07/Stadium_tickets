package org.example.stadium_tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stadium_tickets.entity.Match;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Match Management", description = "APIs for managing football matches")
public class MatchController {

    private final List<Match> matches = new ArrayList<>();

    @GetMapping
    @Operation(summary = "Get all matches", description = "Retrieves a list of all football matches")
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matches);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get match by ID", description = "Retrieves a specific football match by its ID")
    public ResponseEntity<Match> getMatchById(
            @Parameter(description = "ID of the match to retrieve", required = true)
            @PathVariable Long id) {
        // In a real application, this would search the database
        return ResponseEntity.ok(new Match());
    }

    @PostMapping
    @Operation(summary = "Create a new match", description = "Creates a new football match")
    public ResponseEntity<Match> createMatch(
            @Parameter(description = "Match details", required = true)
            @RequestBody Match match) {
        matches.add(match);
        return ResponseEntity.ok(match);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a match", description = "Updates an existing football match")
    public ResponseEntity<Match> updateMatch(
            @Parameter(description = "ID of the match to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated match details", required = true)
            @RequestBody Match match) {
        // In a real application, this would update the database
        return ResponseEntity.ok(match);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a match", description = "Deletes a football match")
    public ResponseEntity<Void> deleteMatch(
            @Parameter(description = "ID of the match to delete", required = true)
            @PathVariable Long id) {
        // In a real application, this would delete from the database
        return ResponseEntity.noContent().build();
    }
}