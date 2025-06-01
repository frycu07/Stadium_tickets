package org.example.stadium_tickets.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.payload.request.LoginRequest;
import org.example.stadium_tickets.payload.request.PasswordlessLoginRequest;
import org.example.stadium_tickets.payload.response.JwtResponse;
import org.example.stadium_tickets.security.JwtUtilsInterface;
import org.example.stadium_tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Authentication API for login and registration")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtilsInterface jwtUtils;

    @Autowired
    public AuthController(UserService userService, AuthenticationManager authenticationManager, JwtUtilsInterface jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Operation(
        summary = "Authenticate user and generate JWT token",
        description = "Authenticates a user with username and password, then returns a JWT token. " +
                "Always use the plain text password (e.g., 'admin'), not the BCrypt hash."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid username or password")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtils.generateJwtToken(authentication);

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            // Don't remove the ROLE_ prefix as Spring Security expects it
            List<String> roles = userDetails.getAuthorities().stream()
                    .map(item -> item.getAuthority())
                    .collect(Collectors.toList());

            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return ResponseEntity.ok(new JwtResponse(jwt, 
                                         user.getId(), 
                                         user.getUsername(), 
                                         user.getEmail(), 
                                         roles));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // Create user with default USER role
            User createdUser = userService.createUser(user);

            // Remove password from response
            createdUser.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register/admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User user) {
        try {
            // Create user with default USER role
            User createdUser = userService.createUser(user);

            // Add ADMIN role
            userService.addAdminRole(createdUser.getId());

            // Remove password from response
            createdUser.setPassword(null);

            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @Operation(
        summary = "Passwordless authentication for admin and user",
        description = "Authenticates a user with username only, then returns a JWT token. " +
                "This endpoint is for passwordless login for admin and user roles."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully authenticated",
            content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "Invalid username")
    })
    @PostMapping("/login/passwordless")
    public ResponseEntity<?> authenticateUserPasswordless(@RequestBody PasswordlessLoginRequest loginRequest) {
        try {
            // Find user by username
            User user = userService.findByUsername(loginRequest.getUsername())
                    .orElseThrow(() -> new RuntimeException("User not found with username: " + loginRequest.getUsername()));

            // Generate JWT token directly from username
            String jwt = jwtUtils.generateTokenFromUsername(user.getUsername());

            // Get user roles
            List<String> roles = user.getRoles().stream()
                    .map(role -> "ROLE_" + role.getName())
                    .collect(Collectors.toList());

            return ResponseEntity.ok(new JwtResponse(jwt, 
                                         user.getId(), 
                                         user.getUsername(), 
                                         user.getEmail(), 
                                         roles));
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Invalid username: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }
}
