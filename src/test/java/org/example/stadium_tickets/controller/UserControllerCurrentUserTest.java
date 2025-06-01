package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.security.TestAuthentication;
import org.example.stadium_tickets.security.TestUserDetails;
import org.example.stadium_tickets.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerCurrentUserTest {

    @Mock
    private UserService userService;

    private TestableUserController userController;

    private User testUser;
    private Authentication authentication;

    // Create a testable subclass of UserController that allows us to inject the Authentication
    static class TestableUserController extends UserController {
        private final Authentication authentication;

        public TestableUserController(UserService userService, Authentication authentication) {
            super(userService);
            this.authentication = authentication;
        }

        @Override
        protected Authentication getAuthentication() {
            return authentication;
        }
    }

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPassword("password");

        // Create a UserDetails instance
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new TestUserDetails("testuser", "password", authorities);

        // Create an Authentication instance
        authentication = new TestAuthentication(userDetails);

        userController = new TestableUserController(userService, authentication);
    }

    @Test
    void getCurrentUser_WhenUserExists_ShouldReturnUser() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<?> response = userController.getCurrentUser();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof User);
        User returnedUser = (User) response.getBody();
        assertEquals("testuser", returnedUser.getUsername());
        assertNull(returnedUser.getPassword()); // Password should be removed
    }

    @Test
    void getCurrentUser_WhenUserDoesNotExist_ShouldReturnError() {
        // Arrange
        when(userService.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.getCurrentUser();

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals("User not found", errorMap.get("error"));
    }

    @Test
    void updateCurrentUser_WhenUserExists_ShouldUpdateAndReturnUser() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setEmail("updated@example.com");
        updatedUser.setPassword("newpassword");

        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userService.updateUser(eq(1L), any(User.class))).thenReturn(testUser);

        // Act
        ResponseEntity<?> response = userController.updateCurrentUser(updatedUser);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof User);
        User returnedUser = (User) response.getBody();
        assertNull(returnedUser.getPassword()); // Password should be removed
    }

    @Test
    void updateCurrentUser_WhenUserDoesNotExist_ShouldReturnError() {
        // Arrange
        User updatedUser = new User();
        updatedUser.setEmail("updated@example.com");

        when(userService.findByUsername("testuser")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.updateCurrentUser(updatedUser);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);
        Map<String, String> errorMap = (Map<String, String>) response.getBody();
        assertEquals("User not found", errorMap.get("error"));
        verify(userService, never()).updateUser(anyLong(), any(User.class));
    }
}
