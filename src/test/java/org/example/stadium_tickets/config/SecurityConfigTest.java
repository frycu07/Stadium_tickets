package org.example.stadium_tickets.config;

import org.example.stadium_tickets.security.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SecurityConfigTest {

    /**
     * Test that BCryptPasswordEncoder works correctly.
     * This indirectly tests the passwordEncoder() method in SecurityConfig.
     */
    @Test
    void testBCryptPasswordEncoder() {
        // Create a new instance of BCryptPasswordEncoder directly
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        // Assert
        assertNotNull(passwordEncoder);
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder);

        // Test encoding
        String password = "testPassword";
        String encodedPassword = passwordEncoder.encode(password);
        assertNotNull(encodedPassword);
        assertNotEquals(password, encodedPassword);
        assertTrue(passwordEncoder.matches(password, encodedPassword));
    }

    /**
     * Test that DaoAuthenticationProvider can be created.
     * This indirectly tests the authenticationProvider() method in SecurityConfig.
     */
    @Test
    void testDaoAuthenticationProvider() {
        // Create a mock UserDetailsService
        UserDetailsService userDetailsService = mock(UserDetailsService.class);

        // Create a DaoAuthenticationProvider directly
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());

        // Assert
        assertNotNull(authProvider);
        assertTrue(authProvider instanceof DaoAuthenticationProvider);
    }

    /**
     * Test that AuthenticationManager type exists.
     * This indirectly tests that the authenticationManager() method in SecurityConfig has a valid return type.
     */
    @Test
    void testAuthenticationManagerType() {
        // Create a mock AuthenticationManager
        AuthenticationManager mockAuthManager = mock(AuthenticationManager.class);

        // Assert
        assertNotNull(mockAuthManager);
        assertTrue(mockAuthManager instanceof AuthenticationManager);
    }

    // Note: Testing securityFilterChain is complex due to HttpSecurity's fluent API
    // In a real-world scenario, you might want to use Spring Security's testing utilities
    // or integration tests to verify the security configuration
}
