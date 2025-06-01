package org.example.stadium_tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.example.stadium_tickets.entity.Stadium;
import org.example.stadium_tickets.service.StadiumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stadiums")
@Tag(name = "Stadium Management", description = "APIs for managing stadiums")
public class StadiumController {

    private final StadiumService stadiumService;

    @Autowired
    public StadiumController(StadiumService stadiumService) {
        this.stadiumService = stadiumService;
    }

    @GetMapping
    @Operation(
        summary = "Get all stadiums", 
        description = "Retrieves a list of all stadiums in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved stadiums",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Stadium.class))
        ),
        @ApiResponse(responseCode = "404", description = "No stadiums found")
    })
    public ResponseEntity<List<Stadium>> getAllStadiums() {
        return ResponseEntity.ok(stadiumService.getAllStadiums());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get stadium by ID", 
        description = "Retrieves a specific stadium by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Stadium found"),
        @ApiResponse(responseCode = "404", description = "Stadium not found")
    })
    public ResponseEntity<Stadium> getStadiumById(
            @Parameter(description = "ID of the stadium to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(stadiumService.getStadiumById(id));
    }

    @PostMapping    
    @Operation(
        summary = "Create a new stadium", 
        description = "Creates a new stadium with the provided details"
    )
    public ResponseEntity<Stadium> createStadium(
            @Parameter(description = "Stadium details", required = true)
            @RequestBody Stadium stadium) {
        return ResponseEntity.ok(stadiumService.createStadium(stadium));
    }

    @PutMapping(path = "/{id}", consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(
        summary = "Update a stadium", 
        description = "Updates an existing stadium with the provided details"
    )
    public ResponseEntity<Stadium> updateStadium(
            @Parameter(description = "ID of the stadium to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated stadium details", required = true)
            @RequestBody Stadium stadium) {
        return ResponseEntity.ok(stadiumService.updateStadium(id, stadium));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a stadium", 
        description = "Deletes a stadium from the system"
    )
    public ResponseEntity<Void> deleteStadium(
            @Parameter(description = "ID of the stadium to delete", required = true)
            @PathVariable Long id) {
        stadiumService.deleteStadium(id);
        return ResponseEntity.noContent().build();
    }
}
