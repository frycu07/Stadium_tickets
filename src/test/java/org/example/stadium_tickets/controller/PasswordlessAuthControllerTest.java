package org.example.stadium_tickets.controller;

import org.example.stadium_tickets.entity.Role;
import org.example.stadium_tickets.entity.User;
import org.example.stadium_tickets.payload.request.PasswordlessLoginRequest;
import org.example.stadium_tickets.repository.RoleRepository;
import org.example.stadium_tickets.repository.UserRepository;
import org.example.stadium_tickets.security.JwtUtilsInterface;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class PasswordlessAuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        // Create test users if they don't exist
        if (!userRepository.existsByUsername("test_admin_passwordless")) {
            User adminUser = new User();
            adminUser.setUsername("test_admin_passwordless");
            adminUser.setEmail("test_admin_passwordless@example.com");
            
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            
            adminUser.addRole(adminRole);
            adminUser.addRole(userRole);
            
            userRepository.save(adminUser);
        }
        
        if (!userRepository.existsByUsername("test_user_passwordless")) {
            User regularUser = new User();
            regularUser.setUsername("test_user_passwordless");
            regularUser.setEmail("test_user_passwordless@example.com");
            
            Role userRole = roleRepository.findByName("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));
            
            regularUser.addRole(userRole);
            
            userRepository.save(regularUser);
        }
    }

    @Test
    public void testPasswordlessLoginForAdmin() throws Exception {
        PasswordlessLoginRequest loginRequest = new PasswordlessLoginRequest("test_admin_passwordless");
        
        MvcResult result = mockMvc.perform(post("/api/auth/login/passwordless")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("test_admin_passwordless"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[?(@=='ROLE_ADMIN')]").exists())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        System.out.println("[DEBUG_LOG] Admin passwordless login response: " + response);
    }

    @Test
    public void testPasswordlessLoginForUser() throws Exception {
        PasswordlessLoginRequest loginRequest = new PasswordlessLoginRequest("test_user_passwordless");
        
        MvcResult result = mockMvc.perform(post("/api/auth/login/passwordless")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("test_user_passwordless"))
                .andExpect(jsonPath("$.roles").isArray())
                .andExpect(jsonPath("$.roles[?(@=='ROLE_USER')]").exists())
                .andReturn();
        
        String response = result.getResponse().getContentAsString();
        System.out.println("[DEBUG_LOG] User passwordless login response: " + response);
    }

    @Test
    public void testPasswordlessLoginWithInvalidUsername() throws Exception {
        PasswordlessLoginRequest loginRequest = new PasswordlessLoginRequest("nonexistent_user");
        
        mockMvc.perform(post("/api/auth/login/passwordless")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}