package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private AuthController authController;
    private User testUser;
    private Role userRole;
    private Role adminRole;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userService);

        // Create test roles
        userRole = new Role("USER");
        userRole.setId(1L);

        adminRole = new Role("ADMIN");
        adminRole.setId(2L);

        // Create test user
        testUser = new User("testuser", "password123", "test@example.com");
        testUser.setId(1L);
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        testUser.setRoles(roles);

        // Setup mock behavior
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(userService.addAdminRole(anyLong())).thenReturn(testUser);
    }

    @Test
    void testRegisterUser_Success() {
        User newUser = new User("newuser", "password123", "new@example.com");

        ResponseEntity<?> response = authController.registerUser(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof User);

        User responseUser = (User) response.getBody();
        assertEquals(testUser.getId(), responseUser.getId());
        assertEquals(testUser.getUsername(), responseUser.getUsername());
        assertEquals(testUser.getEmail(), responseUser.getEmail());

        // Verify that password is removed from response
        assertNull(responseUser.getPassword());

        // Verify the service method was called
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testRegisterUser_Error() {
        User newUser = new User("newuser", "password123", "new@example.com");

        // Mock the service to throw an exception
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Username already exists"));

        ResponseEntity<?> response = authController.registerUser(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Username already exists", errorResponse.get("error"));

        // Verify the service method was called
        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testRegisterAdmin_Success() {
        // Create a new user with ID set
        User newUser = new User("newadmin", "password123", "admin@example.com");
        newUser.setId(1L);

        // Create a user with both USER and ADMIN roles for the response
        User adminUser = new User("newadmin", "password123", "admin@example.com");
        adminUser.setId(1L);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(userRole);
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);

        // Mock the service to return the new user when createUser is called
        when(userService.createUser(any(User.class))).thenReturn(newUser);
        // Mock the service to return the admin user when addAdminRole is called
        when(userService.addAdminRole(1L)).thenReturn(adminUser);

        ResponseEntity<?> response = authController.registerAdmin(newUser);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof User);

        User responseUser = (User) response.getBody();
        assertEquals(adminUser.getId(), responseUser.getId());
        assertEquals(adminUser.getUsername(), responseUser.getUsername());
        assertEquals(adminUser.getEmail(), responseUser.getEmail());

        // Verify that password is removed from response
        assertNull(responseUser.getPassword());

        // Verify the service methods were called
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(1L);
    }

    @Test
    void testRegisterAdmin_ErrorInCreateUser() {
        User newUser = new User("newadmin", "password123", "admin@example.com");

        // Mock the service to throw an exception when creating user
        when(userService.createUser(any(User.class))).thenThrow(new RuntimeException("Username already exists"));

        ResponseEntity<?> response = authController.registerAdmin(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Username already exists", errorResponse.get("error"));

        // Verify the service method was called
        verify(userService, times(1)).createUser(any(User.class));
        // Verify that addAdminRole was not called
        verify(userService, never()).addAdminRole(anyLong());
    }

    @Test
    void testRegisterAdmin_ErrorInAddAdminRole() {
        User newUser = new User("newadmin", "password123", "admin@example.com");

        // Mock the service to throw an exception when adding admin role
        when(userService.addAdminRole(anyLong())).thenThrow(new RuntimeException("Role not found"));

        ResponseEntity<?> response = authController.registerAdmin(newUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Role not found", errorResponse.get("error"));

        // Verify the service methods were called
        verify(userService, times(1)).createUser(any(User.class));
        verify(userService, times(1)).addAdminRole(anyLong());
    }
}
