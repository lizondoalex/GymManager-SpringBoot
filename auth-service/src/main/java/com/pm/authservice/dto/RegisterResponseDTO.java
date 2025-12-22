package com.pm.authservice.dto;

public class RegisterResponseDTO {

    private String token;

    public RegisterResponseDTO(String token){
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
