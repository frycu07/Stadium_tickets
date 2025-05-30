package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.repository.RoleRepository;
import org.example.stadium_tickets.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
    }

    @Override
    @Transactional
    public Role createRole(Role role) {
        if (existsByName(role.getName())) {
            throw new RuntimeException("Role already exists with name: " + role.getName());
        }
        return roleRepository.save(role);
    }

    @Override
    @Transactional
    public Role updateRole(Long id, Role role) {
        Role existingRole = getRoleById(id);
        
        // Check if name is being changed and if it already exists
        if (!existingRole.getName().equals(role.getName()) && existsByName(role.getName())) {
            throw new RuntimeException("Role already exists with name: " + role.getName());
        }
        
        // Update fields
        existingRole.setName(role.getName());
        
        return roleRepository.save(existingRole);
    }

    @Override
    @Transactional
    public void deleteRole(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return roleRepository.findByName(name);
    }

    @Override
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}