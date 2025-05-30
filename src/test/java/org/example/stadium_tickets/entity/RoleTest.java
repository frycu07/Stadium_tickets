package org.example.stadium_tickets.entity;

import org.junit.jupiter.api.Test;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @Test
    void testDefaultConstructor() {
        Role role = new Role();
        assertNull(role.getId());
        assertNull(role.getName());
        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }

    @Test
    void testParameterizedConstructor() {
        String name = "ADMIN";
        Role role = new Role(name);
        
        assertNull(role.getId());
        assertEquals(name, role.getName());
        assertNotNull(role.getUsers());
        assertTrue(role.getUsers().isEmpty());
    }

    @Test
    void testSettersAndGetters() {
        Role role = new Role();
        
        Long id = 1L;
        role.setId(id);
        assertEquals(id, role.getId());
        
        String name = "USER";
        role.setName(name);
        assertEquals(name, role.getName());
        
        Set<User> users = new HashSet<>();
        User user = new User("testuser", "password", "test@example.com");
        users.add(user);
        role.setUsers(users);
        assertEquals(users, role.getUsers());
        assertEquals(1, role.getUsers().size());
    }
}