package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.repository.RoleRepository;
import org.example.stadium_tickets.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private UserServiceImpl userService;
    private User testUser;
    private Role testRole;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, roleRepository, passwordEncoder);

        testRole = new Role("ROLE_USER");
        testRole.setId(1L);

        testUser = new User("testuser", "password123", "test@example.com");
        testUser.setId(1L);
        Set<Role> roles = new HashSet<>();
        roles.add(testRole);
        testUser.setRoles(roles);

        // Setup mock behavior
        when(userRepository.findAll()).thenReturn(new ArrayList<>());
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(User.class))).thenReturn(testUser);
    }

    @Test
    void testGetAllUsers() {
        // Initially the list should be empty
        List<User> users = userService.getAllUsers();
        assertNotNull(users);
        assertTrue(users.isEmpty());

        // Add a user and check if it's returned
        List<User> userList = new ArrayList<>();
        userList.add(testUser);
        when(userRepository.findAll()).thenReturn(userList);

        users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals(testUser, users.get(0));
    }

    @Test
    void testGetUserById() {
        User user = userService.getUserById(1L);
        assertNotNull(user);
        assertEquals(testUser, user);
    }

    @Test
    void testGetUserByIdNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.getUserById(99L);
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testCreateUser() {
        User newUser = new User("newuser", "newpassword", "new@example.com");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        // Mock the role repository to return a role when findByName("USER") is called
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(testRole));

        // Mock the password encoder
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User createdUser = userService.createUser(newUser);
        assertNotNull(createdUser);
        assertEquals(newUser, createdUser);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserWithExistingUsername() {
        User newUser = new User("testuser", "newpassword", "new@example.com");
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });

        assertTrue(exception.getMessage().contains("Username already exists"));
    }

    @Test
    void testCreateUserWithExistingEmail() {
        User newUser = new User("newuser", "newpassword", "test@example.com");
        when(userRepository.existsByUsername("newuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.createUser(newUser);
        });

        assertTrue(exception.getMessage().contains("Email already exists"));
    }

    @Test
    void testUpdateUser() {
        User updatedUser = new User("updateduser", "updatedpassword", "updated@example.com");
        updatedUser.setId(1L);

        when(userRepository.existsByUsername("updateduser")).thenReturn(false);
        when(userRepository.existsByEmail("updated@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // Mock the password encoder
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        User result = userService.updateUser(1L, updatedUser);
        assertNotNull(result);
        assertEquals(updatedUser, result);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUserWithExistingUsername() {
        User updatedUser = new User("existinguser", "updatedpassword", "updated@example.com");
        updatedUser.setId(1L);

        when(userRepository.existsByUsername("existinguser")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertTrue(exception.getMessage().contains("Username already exists"));
    }

    @Test
    void testUpdateUserWithExistingEmail() {
        User updatedUser = new User("updateduser", "updatedpassword", "existing@example.com");
        updatedUser.setId(1L);

        when(userRepository.existsByUsername("updateduser")).thenReturn(false);
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.updateUser(1L, updatedUser);
        });

        assertTrue(exception.getMessage().contains("Email already exists"));
    }

    @Test
    void testDeleteUser() {
        when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUser(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUserNotFound() {
        when(userRepository.existsById(99L)).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.deleteUser(99L);
        });

        assertTrue(exception.getMessage().contains("User not found"));
    }

    @Test
    void testFindByUsername() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByUsername("testuser");
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    void testFindByEmail() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(testUser));

        Optional<User> result = userService.findByEmail("test@example.com");
        assertTrue(result.isPresent());
        assertEquals(testUser, result.get());
    }

    @Test
    void testExistsByUsername() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);
        when(userRepository.existsByUsername("nonexistent")).thenReturn(false);

        assertTrue(userService.existsByUsername("testuser"));
        assertFalse(userService.existsByUsername("nonexistent"));
    }

    @Test
    void testExistsByEmail() {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);
        when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        assertTrue(userService.existsByEmail("test@example.com"));
        assertFalse(userService.existsByEmail("nonexistent@example.com"));
    }
}
