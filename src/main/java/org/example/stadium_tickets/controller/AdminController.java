package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        // Remove passwords from response
        users.forEach(user -> user.setPassword(null));
        return ResponseEntity.ok(users);
    }

    @PutMapping("/users/{userId}/roles/admin")
    public ResponseEntity<User> addAdminRole(@PathVariable Long userId) {
        User user = userService.addAdminRole(userId);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{userId}/roles/admin")
    public ResponseEntity<User> removeAdminRole(@PathVariable Long userId) {
        User user = userService.removeAdminRole(userId);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/users/{userId}/roles/user")
    public ResponseEntity<User> addUserRole(@PathVariable Long userId) {
        User user = userService.addUserRole(userId);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/users/{userId}/roles/user")
    public ResponseEntity<User> removeUserRole(@PathVariable Long userId) {
        User user = userService.removeUserRole(userId);
        user.setPassword(null);
        return ResponseEntity.ok(user);
    }
}