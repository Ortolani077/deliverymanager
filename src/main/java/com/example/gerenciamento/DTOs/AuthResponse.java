package com.example.gerenciamento.DTOs;

public class AuthResponse {

    private String token;

    // Construtor
    public AuthResponse(String token) {
        this.token = token;
    }

    // Getter
    public String getToken() {
        return token;
    }

    // Setter
    public void setToken(String token) {
        this.token = token;
    }
}
