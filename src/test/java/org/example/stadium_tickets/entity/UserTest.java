package org.example.stadium_tickets.entity;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getPassword());
        assertNull(user.getEmail());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        String username = "testuser";
        String password = "password123";
        String email = "test@example.com";
        
        User user = new User(username, password, email);
        
        assertNull(user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(password, user.getPassword());
        assertEquals(email, user.getEmail());
        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getRoles());
        assertTrue(user.getRoles().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();
        
        Long id = 1L;
        user.setId(id);
        assertEquals(id, user.getId());
        
        String username = "newuser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());
        
        String password = "newpassword";
        user.setPassword(password);
        assertEquals(password, user.getPassword());
        
        String email = "new@example.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());
        
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        user.setCreatedAt(createdAt);
        assertEquals(createdAt, user.getCreatedAt());
        
        Set<Role> roles = new HashSet<>();
        Role role = new Role("USER");
        roles.add(role);
        user.setRoles(roles);
        assertEquals(roles, user.getRoles());
        assertEquals(1, user.getRoles().size());
    }
    
    @Test
    void testAddRole() {
        User user = new User();
        Role role = new Role("ADMIN");
        
        user.addRole(role);
        
        assertTrue(user.getRoles().contains(role));
        assertEquals(1, user.getRoles().size());
        
        // Add the same role again
        user.addRole(role);
        assertEquals(1, user.getRoles().size(), "Adding the same role twice should not create duplicates");
        
        // Add another role
        Role role2 = new Role("USER");
        user.addRole(role2);
        assertEquals(2, user.getRoles().size());
        assertTrue(user.getRoles().contains(role2));
    }
}