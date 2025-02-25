package com.fjtm.campeonato.dto;

public class LoginRequest {
    private String username;
    private String password;

    // Constructor vacío (necesario para la deserialización JSON)
    public LoginRequest() {
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

