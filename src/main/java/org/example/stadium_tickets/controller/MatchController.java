package org.example.stadium_tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stadium_tickets.entity.Match;
import org.example.stadium_tickets.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
@Tag(name = "Match Management", description = "APIs for managing football matches")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    @Operation(summary = "Get all matches", description = "Retrieves a list of all football matches")
    public ResponseEntity<List<Match>> getAllMatches() {
        return ResponseEntity.ok(matchService.getAllMatches());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get match by ID", description = "Retrieves a specific football match by its ID")
    public ResponseEntity<Match> getMatchById(
            @Parameter(description = "ID of the match to retrieve", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(matchService.getMatchById(id));
    }

    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(summary = "Create a new match", description = "Creates a new football match")
    public ResponseEntity<Match> createMatch(
            @Parameter(description = "Match details", required = true)
            @RequestBody Match match) {
        return ResponseEntity.ok(matchService.createMatch(match));
    }

    @PutMapping(path = "/{id}", consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(summary = "Update a match", description = "Updates an existing football match")
    public ResponseEntity<Match> updateMatch(
            @Parameter(description = "ID of the match to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated match details", required = true)
            @RequestBody Match match) {
        return ResponseEntity.ok(matchService.updateMatch(id, match));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a match", description = "Deletes a football match")
    public ResponseEntity<Void> deleteMatch(
            @Parameter(description = "ID of the match to delete", required = true)
            @PathVariable Long id) {
        matchService.deleteMatch(id);
        return ResponseEntity.noContent().build();
    }
}
