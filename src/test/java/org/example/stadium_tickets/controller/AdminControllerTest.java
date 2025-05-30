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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    private AdminController adminController;
    private User testUser;
    private Role userRole;
    private Role adminRole;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(userService);

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
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        when(userService.addAdminRole(anyLong())).thenReturn(testUser);
        when(userService.removeAdminRole(anyLong())).thenReturn(testUser);
        when(userService.addUserRole(anyLong())).thenReturn(testUser);
        when(userService.removeUserRole(anyLong())).thenReturn(testUser);
    }

    @Test
    void testGetAllUsers() {
        // Initially the list should be empty
        ResponseEntity<List<User>> response = adminController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Add a user and check if it's returned
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        response = adminController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testUser, response.getBody().get(0));
        
        // Verify that passwords are removed from the response
        assertNull(response.getBody().get(0).getPassword());
    }

    @Test
    void testAddAdminRole() {
        // Setup the user with admin role
        Set<Role> rolesWithAdmin = new HashSet<>();
        rolesWithAdmin.add(userRole);
        rolesWithAdmin.add(adminRole);
        
        User userWithAdminRole = new User("testuser", "password123", "test@example.com");
        userWithAdminRole.setId(1L);
        userWithAdminRole.setRoles(rolesWithAdmin);
        
        when(userService.addAdminRole(1L)).thenReturn(userWithAdminRole);

        ResponseEntity<User> response = adminController.addAdminRole(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verify that the user has the admin role
        assertTrue(response.getBody().getRoles().contains(adminRole));
        
        // Verify that password is removed from the response
        assertNull(response.getBody().getPassword());
        
        // Verify the service method was called
        verify(userService, times(1)).addAdminRole(1L);
    }

    @Test
    void testRemoveAdminRole() {
        // Setup the user without admin role
        Set<Role> rolesWithoutAdmin = new HashSet<>();
        rolesWithoutAdmin.add(userRole);
        
        User userWithoutAdminRole = new User("testuser", "password123", "test@example.com");
        userWithoutAdminRole.setId(1L);
        userWithoutAdminRole.setRoles(rolesWithoutAdmin);
        
        when(userService.removeAdminRole(1L)).thenReturn(userWithoutAdminRole);

        ResponseEntity<User> response = adminController.removeAdminRole(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verify that the user doesn't have the admin role
        assertFalse(response.getBody().getRoles().contains(adminRole));
        
        // Verify that password is removed from the response
        assertNull(response.getBody().getPassword());
        
        // Verify the service method was called
        verify(userService, times(1)).removeAdminRole(1L);
    }

    @Test
    void testAddUserRole() {
        // Setup the user with user role
        Set<Role> rolesWithUser = new HashSet<>();
        rolesWithUser.add(userRole);
        
        User userWithUserRole = new User("testuser", "password123", "test@example.com");
        userWithUserRole.setId(1L);
        userWithUserRole.setRoles(rolesWithUser);
        
        when(userService.addUserRole(1L)).thenReturn(userWithUserRole);

        ResponseEntity<User> response = adminController.addUserRole(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verify that the user has the user role
        assertTrue(response.getBody().getRoles().contains(userRole));
        
        // Verify that password is removed from the response
        assertNull(response.getBody().getPassword());
        
        // Verify the service method was called
        verify(userService, times(1)).addUserRole(1L);
    }

    @Test
    void testRemoveUserRole() {
        // Setup the user without user role
        Set<Role> rolesWithoutUser = new HashSet<>();
        rolesWithoutUser.add(adminRole);
        
        User userWithoutUserRole = new User("testuser", "password123", "test@example.com");
        userWithoutUserRole.setId(1L);
        userWithoutUserRole.setRoles(rolesWithoutUser);
        
        when(userService.removeUserRole(1L)).thenReturn(userWithoutUserRole);

        ResponseEntity<User> response = adminController.removeUserRole(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        
        // Verify that the user doesn't have the user role
        assertFalse(response.getBody().getRoles().contains(userRole));
        
        // Verify that password is removed from the response
        assertNull(response.getBody().getPassword());
        
        // Verify the service method was called
        verify(userService, times(1)).removeUserRole(1L);
    }
}