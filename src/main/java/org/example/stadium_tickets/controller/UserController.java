package org.example.stadium_tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    protected Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    @GetMapping
    @Operation(
        summary = "Get all users", 
        description = "Retrieves a list of all users in the system",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Successfully retrieved users",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = User.class))
        ),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    @Operation(
        summary = "Get user by ID", 
        description = "Retrieves a specific user by their ID",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<User> getUserById(
            @Parameter(description = "ID of the user to retrieve", required = true, example = "1")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PostMapping(consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(
        summary = "Create a new user", 
        description = "Creates a new user with the provided details"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<User> createUser(
            @Parameter(description = "User details", required = true)
            @RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @PutMapping(path = "/{id}", consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(
        summary = "Update a user", 
        description = "Updates an existing user with the provided details",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    public ResponseEntity<User> updateUser(
            @Parameter(description = "ID of the user to update", required = true)
            @PathVariable Long id,
            @Parameter(description = "Updated user details", required = true)
            @RequestBody User user) {
        return ResponseEntity.ok(userService.updateUser(id, user));
    }

    @GetMapping("/me")
    @Operation(
        summary = "Get current user", 
        description = "Retrieves the currently authenticated user's information",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> getCurrentUser() {
        Authentication authentication = getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setPassword(null); // Remove password from response
            return ResponseEntity.ok(user);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping(path = "/me", consumes = {"application/json", "application/json;charset=UTF-8"})
    @Operation(
        summary = "Update current user", 
        description = "Updates the currently authenticated user's information",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<?> updateCurrentUser(@RequestBody User user) {
        Authentication authentication = getAuthentication();
        String username = authentication.getName();

        Optional<User> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            User existingUser = userOptional.get();

            // Only allow updating certain fields
            if (user.getEmail() != null) {
                existingUser.setEmail(user.getEmail());
            }
            if (user.getPassword() != null) {
                // Password will be encoded in the service
                existingUser.setPassword(user.getPassword());
            }

            User updatedUser = userService.updateUser(existingUser.getId(), existingUser);
            updatedUser.setPassword(null); // Remove password from response

            return ResponseEntity.ok(updatedUser);
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "User not found");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a user", 
        description = "Deletes a user from the system",
        security = { @SecurityRequirement(name = "bearer-key") }
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "401", description = "Unauthorized"),
        @ApiResponse(responseCode = "403", description = "Forbidden")
    })
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID of the user to delete", required = true)
            @PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
