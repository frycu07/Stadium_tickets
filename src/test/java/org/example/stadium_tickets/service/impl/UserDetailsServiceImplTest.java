package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserDetailsServiceImplTest {

    private UserDetailsServiceImpl userDetailsService;
    private User testUser;
    private Role userRole;
    private Role adminRole;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userDetailsService = new UserDetailsServiceImpl(userRepository);

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
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());
    }

    @Test
    void testLoadUserByUsername_Success() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("testuser");
        
        assertNotNull(userDetails);
        assertEquals(testUser.getUsername(), userDetails.getUsername());
        assertEquals(testUser.getPassword(), userDetails.getPassword());
        
        // Verify authorities
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(1, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        
        // Verify the repository method was called
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void testLoadUserByUsername_UserWithMultipleRoles() {
        // Create a user with multiple roles
        User adminUser = new User("adminuser", "password123", "admin@example.com");
        adminUser.setId(2L);
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(userRole);
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles);
        
        when(userRepository.findByUsername("adminuser")).thenReturn(Optional.of(adminUser));
        
        UserDetails userDetails = userDetailsService.loadUserByUsername("adminuser");
        
        assertNotNull(userDetails);
        assertEquals(adminUser.getUsername(), userDetails.getUsername());
        assertEquals(adminUser.getPassword(), userDetails.getPassword());
        
        // Verify authorities
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertNotNull(authorities);
        assertEquals(2, authorities.size());
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(authorities.stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        
        // Verify the repository method was called
        verify(userRepository, times(1)).findByUsername("adminuser");
    }

    @Test
    void testLoadUserByUsername_UserNotFound() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
        
        assertTrue(exception.getMessage().contains("User not found with username: nonexistent"));
        
        // Verify the repository method was called
        verify(userRepository, times(1)).findByUsername("nonexistent");
    }
}