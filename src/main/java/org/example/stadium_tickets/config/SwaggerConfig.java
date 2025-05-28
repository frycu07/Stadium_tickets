package org.example.stadium_tickets.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI stadiumTicketsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Stadium Tickets API")
                        .description("API for managing stadium tickets")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Stadium Tickets Team")
                                .email("contact@stadiumtickets.com")))
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token authentication")));
    }
}
