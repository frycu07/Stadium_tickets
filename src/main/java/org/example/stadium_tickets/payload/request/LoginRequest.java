package org.example.stadium_tickets.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Login request with username and password")
public class LoginRequest {
    @Schema(description = "Username for authentication", example = "admin")
    private String username;

    @Schema(description = "Plain text password (not BCrypt hash)", example = "admin")
    private String password;

    public LoginRequest() {
    }

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
