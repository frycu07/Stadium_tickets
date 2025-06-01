package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.payload.request.LoginRequest;
import org.example.stadium_tickets.payload.response.JwtResponse;
import org.example.stadium_tickets.security.JwtUtilsInterface;
import org.example.stadium_tickets.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

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

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtilsInterface jwtUtils;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(userService, authenticationManager, jwtUtils);

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

    @Test
    void testLogin_Success() {
        // Create login request
        LoginRequest loginRequest = new LoginRequest("testuser", "password123");

        // Create user details with authorities
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new org.example.stadium_tickets.security.TestUserDetails("testuser", "password123", authorities);

        // Create test authentication with the mock user details
        Authentication authentication = new org.example.stadium_tickets.security.TestAuthentication(userDetails);

        // Setup authentication manager mock
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        // Setup JWT mock
        when(jwtUtils.generateJwtToken(authentication)).thenReturn("test-jwt-token");

        // Setup user service mock
        when(userService.findByUsername("testuser")).thenReturn(Optional.of(testUser));

        // Call the login method
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Verify response
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof JwtResponse);

        JwtResponse jwtResponse = (JwtResponse) response.getBody();
        assertEquals("test-jwt-token", jwtResponse.getToken());
        assertEquals(testUser.getId(), jwtResponse.getId());
        assertEquals(testUser.getUsername(), jwtResponse.getUsername());
        assertEquals(testUser.getEmail(), jwtResponse.getEmail());
        assertTrue(jwtResponse.getRoles().contains("ROLE_USER"));

        // Verify method calls
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, times(1)).generateJwtToken(authentication);
        verify(userService, times(1)).findByUsername("testuser");
    }

    @Test
    void testLogin_Failure() {
        // Create login request
        LoginRequest loginRequest = new LoginRequest("testuser", "wrongpassword");

        // Setup authentication manager to throw exception
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("Bad credentials"));

        // Call the login method
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);

        // Verify response
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, String> errorResponse = (Map<String, String>) response.getBody();
        assertEquals("Invalid username or password", errorResponse.get("error"));

        // Verify method calls
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtils, never()).generateJwtToken(any());
        verify(userService, never()).findByUsername(anyString());
    }
}
