package org.example.stadium_tickets.service;

import org.example.stadium_tickets.entity.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    List<Role> getAllRoles();
    Role getRoleById(Long id);
    Role createRole(Role role);
    Role updateRole(Long id, Role role);
    void deleteRole(Long id);
    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}