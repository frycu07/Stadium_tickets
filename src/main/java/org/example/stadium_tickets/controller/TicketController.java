package org.example.stadium_tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.stadium_tickets.entity.Ticket;
import org.example.stadium_tickets.service.TicketService;
import org.example.stadium_tickets.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@Tag(name = "Ticket Management", description = "APIs for managing stadium tickets")
public class TicketController {

    private final TicketService ticketService;
    private final MatchService matchService;

    @Autowired
    public TicketController(TicketService ticketService, MatchService matchService) {
        this.ticketService = ticketService;
        this.matchService = matchService;
    }

    @GetMapping
    @Operation(
        summary = "Get all tickets", 
        description = "Retrieves a list of all tickets in the system",
        tags = {"Ticket Management"}
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved tickets",
            content = @Content(
                mediaType = "application/json", 
                array = @ArraySchema(schema = @Schema(implementation = Ticket.class))
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "No tickets found",
            content = @Content
        )
    })
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.getAllTickets());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get ticket by ID", 
        description = "Retrieves a specific ticket by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Ticket found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Ticket.class))
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "Ticket not found",
            content = @Content
        )
    })
    public ResponseEntity<Ticket> getTicketById(
            @Parameter(description = "ID of the ticket to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(ticketService.getTicketById(id));
    }

    @GetMapping("/match/{matchId}")
    @Operation(
        summary = "Get tickets by match ID", 
        description = "Retrieves all tickets for a specific match"
    )
    public ResponseEntity<List<Ticket>> getTicketsByMatchId(
            @Parameter(description = "ID of the match", required = true, example = "1")
            @PathVariable Long matchId) {
        return ResponseEntity.ok(ticketService.findByMatch(matchService.getMatchById(matchId)));
    }



    @PostMapping
    @Operation(
        summary = "Purchase a ticket", 
        description = "Purchases a new ticket for a match"
    )
    public ResponseEntity<Ticket> purchaseTicket(
            @RequestBody Ticket ticket) {
        return ResponseEntity.ok(ticketService.createTicket(ticket));
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Cancel a ticket", 
        description = "Cancels a purchased ticket",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    public ResponseEntity<Void> cancelTicket(
            @Parameter(description = "ID of the ticket to cancel", required = true)
            @PathVariable Long id) {
        ticketService.cancelTicket(id);
        return ResponseEntity.noContent().build();
    }
}
