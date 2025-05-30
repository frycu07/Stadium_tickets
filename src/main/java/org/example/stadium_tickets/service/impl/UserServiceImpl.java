package org.example.stadium_tickets.service.impl;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.repository.RoleRepository;
import org.example.stadium_tickets.repository.UserRepository;
import org.example.stadium_tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    @Transactional
    public User createUser(User user) {
        if (existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }
        if (existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Add default USER role if no roles are specified
        if (user.getRoles() == null || user.getRoles().isEmpty()) {
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Default role USER not found"));
            user.addRole(userRole);
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User user) {
        User existingUser = getUserById(id);

        // Check if username is being changed and if it already exists
        if (!existingUser.getUsername().equals(user.getUsername()) && existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists: " + user.getUsername());
        }

        // Check if email is being changed and if it already exists
        if (!existingUser.getEmail().equals(user.getEmail()) && existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists: " + user.getEmail());
        }

        // Update fields
        existingUser.setUsername(user.getUsername());

        // Encode password if it's being changed
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        existingUser.setEmail(user.getEmail());

        // Update roles if provided
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            existingUser.setRoles(user.getRoles());
        }

        return userRepository.save(existingUser);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public User addUserRole(Long userId) {
        User user = getUserById(userId);
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        user.addRole(userRole);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User addAdminRole(Long userId) {
        User user = getUserById(userId);
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        user.addRole(adminRole);
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeUserRole(Long userId) {
        User user = getUserById(userId);
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Role USER not found"));

        user.getRoles().removeIf(role -> role.getName().equals("USER"));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User removeAdminRole(Long userId) {
        User user = getUserById(userId);
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

        user.getRoles().removeIf(role -> role.getName().equals("ADMIN"));
        return userRepository.save(user);
    }
}
