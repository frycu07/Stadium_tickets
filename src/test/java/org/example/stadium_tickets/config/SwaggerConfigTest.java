package org.example.stadium_tickets.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SwaggerConfigTest {

    @Test
    void testStadiumTicketsOpenAPI() {
        SwaggerConfig swaggerConfig = new SwaggerConfig();
        OpenAPI openAPI = swaggerConfig.stadiumTicketsOpenAPI();
        
        // Test Info object
        Info info = openAPI.getInfo();
        assertNotNull(info);
        assertEquals("Stadium Tickets API", info.getTitle());
        assertEquals("API for managing stadium tickets", info.getDescription());
        assertEquals("1.0.0", info.getVersion());
        
        // Test Contact object
        Contact contact = info.getContact();
        assertNotNull(contact);
        assertEquals("Stadium Tickets Team", contact.getName());
        assertEquals("contact@stadiumtickets.com", contact.getEmail());
        
        // Test Security Scheme
        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearer-key");
        assertNotNull(securityScheme);
        assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        assertEquals("bearer", securityScheme.getScheme());
        assertEquals("JWT", securityScheme.getBearerFormat());
        assertEquals("JWT token authentication", securityScheme.getDescription());
    }
}