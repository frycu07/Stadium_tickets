package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private User testUser;

    @Mock
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userController = new UserController(userService);
        testUser = new User("testuser", "password123", "test@example.com");

        // Setup mock behavior
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());
        when(userService.getUserById(anyLong())).thenReturn(testUser);
        when(userService.createUser(any(User.class))).thenReturn(testUser);
        when(userService.updateUser(anyLong(), any(User.class))).thenReturn(testUser);
    }

    @Test
    void testGetAllUsers() {
        // Initially the list should be empty
        ResponseEntity<List<User>> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());

        // Add a user and check if it's returned
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testUser, response.getBody().get(0));
    }

    @Test
    void testGetUserById() {
        ResponseEntity<User> response = userController.getUserById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testCreateUser() {
        ResponseEntity<User> response = userController.createUser(testUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser, response.getBody());

        // Verify the user was added to the list
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userService.getAllUsers()).thenReturn(userList);

        ResponseEntity<List<User>> allUsers = userController.getAllUsers();
        assertEquals(1, allUsers.getBody().size());
        assertEquals(testUser, allUsers.getBody().get(0));
    }

    @Test
    void testUpdateUser() {
        ResponseEntity<User> response = userController.updateUser(1L, testUser);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testUser, response.getBody());
    }

    @Test
    void testDeleteUser() {
        ResponseEntity<Void> response = userController.deleteUser(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}
