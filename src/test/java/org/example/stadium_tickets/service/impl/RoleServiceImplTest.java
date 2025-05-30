package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    private RoleServiceImpl roleService;
    private Role testRole;

    @Mock
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        roleService = new RoleServiceImpl(roleRepository);
        
        testRole = new Role("ROLE_USER");
        testRole.setId(1L);
        
        // Setup mock behavior
        when(roleRepository.findAll()).thenReturn(new ArrayList<>());
        when(roleRepository.findById(anyLong())).thenReturn(Optional.of(testRole));
        when(roleRepository.save(any(Role.class))).thenReturn(testRole);
    }

    @Test
    void testGetAllRoles() {
        // Initially the list should be empty
        List<Role> roles = roleService.getAllRoles();
        assertNotNull(roles);
        assertTrue(roles.isEmpty());

        // Add a role and check if it's returned
        List<Role> roleList = new ArrayList<>();
        roleList.add(testRole);
        when(roleRepository.findAll()).thenReturn(roleList);
        
        roles = roleService.getAllRoles();
        assertEquals(1, roles.size());
        assertEquals(testRole, roles.get(0));
    }

    @Test
    void testGetRoleById() {
        Role role = roleService.getRoleById(1L);
        assertNotNull(role);
        assertEquals(testRole, role);
    }

    @Test
    void testGetRoleByIdNotFound() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            roleService.getRoleById(99L);
        });
        
        assertTrue(exception.getMessage().contains("Role not found"));
    }

    @Test
    void testCreateRole() {
        Role newRole = new Role("ROLE_ADMIN");
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);
        
        Role createdRole = roleService.createRole(newRole);
        assertNotNull(createdRole);
        assertEquals(newRole, createdRole);
        
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testCreateRoleWithExistingName() {
        Role newRole = new Role("ROLE_USER");
        when(roleRepository.existsByName("ROLE_USER")).thenReturn(true);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            roleService.createRole(newRole);
        });
        
        assertTrue(exception.getMessage().contains("Role already exists with name"));
    }

    @Test
    void testUpdateRole() {
        Role updatedRole = new Role("ROLE_ADMIN");
        updatedRole.setId(1L);
        
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);
        
        Role result = roleService.updateRole(1L, updatedRole);
        assertNotNull(result);
        assertEquals(updatedRole, result);
        
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testUpdateRoleWithExistingName() {
        Role updatedRole = new Role("ROLE_ADMIN");
        updatedRole.setId(1L);
        
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(true);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            roleService.updateRole(1L, updatedRole);
        });
        
        assertTrue(exception.getMessage().contains("Role already exists with name"));
    }

    @Test
    void testDeleteRole() {
        when(roleRepository.existsById(1L)).thenReturn(true);
        
        roleService.deleteRole(1L);
        
        verify(roleRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteRoleNotFound() {
        when(roleRepository.existsById(99L)).thenReturn(false);
        
        Exception exception = assertThrows(RuntimeException.class, () -> {
            roleService.deleteRole(99L);
        });
        
        assertTrue(exception.getMessage().contains("Role not found"));
    }

    @Test
    void testFindByName() {
        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(testRole));
        
        Optional<Role> result = roleService.findByName("ROLE_USER");
        assertTrue(result.isPresent());
        assertEquals(testRole, result.get());
    }

    @Test
    void testExistsByName() {
        when(roleRepository.existsByName("ROLE_USER")).thenReturn(true);
        when(roleRepository.existsByName("ROLE_ADMIN")).thenReturn(false);
        
        assertTrue(roleService.existsByName("ROLE_USER"));
        assertFalse(roleService.existsByName("ROLE_ADMIN"));
    }
}