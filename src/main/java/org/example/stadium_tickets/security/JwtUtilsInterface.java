package org.example.stadium_tickets.security;

import org.springframework.security.core.Authentication;

public interface JwtUtilsInterface {
    String generateJwtToken(Authentication authentication);
    String generateTokenFromUsername(String username);
    String getUsernameFromJwtToken(String token);
    boolean validateJwtToken(String authToken);
}