package org.example.stadium_tickets.controller.advice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthExceptionHandlerTest {

    private AuthExceptionHandler authExceptionHandler;

    @BeforeEach
    void setUp() {
        authExceptionHandler = new AuthExceptionHandler();
    }

    @Test
    void handleAccessDeniedException_ShouldReturnForbiddenStatus() {
        // Arrange
        AccessDeniedException exception = new AccessDeniedException("Access denied test");

        // Act
        ResponseEntity<Map<String, String>> response = authExceptionHandler.handleAccessDeniedException(exception);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Access denied. Make sure you have the required role and are using a valid JWT token in the Authorization header (Bearer <token>).", 
                response.getBody().get("error"));
        assertEquals("Access denied test", response.getBody().get("message"));
    }

    @Test
    void handleAuthenticationException_ShouldReturnUnauthorizedStatus() {
        // Arrange
        AuthenticationException exception = new BadCredentialsException("Bad credentials test");

        // Act
        ResponseEntity<Map<String, String>> response = authExceptionHandler.handleAuthenticationException(exception);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Authentication failed. Please check your credentials or JWT token.", 
                response.getBody().get("error"));
        assertEquals("Bad credentials test", response.getBody().get("message"));
    }

    @Test
    void handleGenericException_ShouldReturnInternalServerErrorStatus() {
        // Arrange
        Exception exception = new RuntimeException("Generic exception test");

        // Act
        ResponseEntity<Map<String, String>> response = authExceptionHandler.handleGenericException(exception);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred.", response.getBody().get("error"));
        assertEquals("Generic exception test", response.getBody().get("message"));
    }
}