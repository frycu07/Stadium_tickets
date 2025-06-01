package org.example.stadium_tickets.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Passwordless login request with username only")
public class PasswordlessLoginRequest {
    @Schema(description = "Username for passwordless authentication", example = "admin")
    private String username;

    public PasswordlessLoginRequest() {
    }

    public PasswordlessLoginRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}