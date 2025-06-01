package org.example.stadium_tickets.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtUtilsTest {

    private JwtUtils jwtUtils;
    private final String testSecret = "testSecretKeyWhichIsAtLeast32BytesLongForTesting";
    private final int testExpirationMs = 60000; // 1 minute
    private final String testUsername = "testuser";

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", testSecret);
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", testExpirationMs);
    }

    @Test
    void testGenerateJwtToken() {
        // Arrange
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new TestUserDetails(testUsername, "password", authorities);
        Authentication authentication = new TestAuthentication(userDetails);

        // Act
        String token = jwtUtils.generateJwtToken(authentication);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals(testUsername, jwtUtils.getUsernameFromJwtToken(token));
    }

    @Test
    void testGenerateTokenFromUsername() {
        // Act
        String token = jwtUtils.generateTokenFromUsername(testUsername);

        // Assert
        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertEquals(testUsername, jwtUtils.getUsernameFromJwtToken(token));
    }

    @Test
    void testGetUsernameFromJwtToken() {
        // Arrange
        String token = jwtUtils.generateTokenFromUsername(testUsername);

        // Act
        String username = jwtUtils.getUsernameFromJwtToken(token);

        // Assert
        assertEquals(testUsername, username);
    }

    @Test
    void testValidateJwtToken_ValidToken() {
        // Arrange
        String token = jwtUtils.generateTokenFromUsername(testUsername);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertTrue(isValid);
    }

    @Test
    void testValidateJwtToken_InvalidSignature() {
        // Arrange
        JwtUtils tempJwtUtils = new JwtUtils();
        ReflectionTestUtils.setField(tempJwtUtils, "jwtSecret", "differentSecretKeyWhichIsAtLeast32BytesLong");
        ReflectionTestUtils.setField(tempJwtUtils, "jwtExpirationMs", testExpirationMs);
        
        String token = tempJwtUtils.generateTokenFromUsername(testUsername);

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_MalformedToken() {
        // Arrange
        String token = "malformedToken";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_EmptyToken() {
        // Arrange
        String token = "";

        // Act
        boolean isValid = jwtUtils.validateJwtToken(token);

        // Assert
        assertFalse(isValid);
    }

    @Test
    void testValidateJwtToken_NullToken() {
        // Act
        boolean isValid = jwtUtils.validateJwtToken(null);

        // Assert
        assertFalse(isValid);
    }
}