package com.example.virtualbank.auth.request;

public record AuthRequestDTO(
        String name,
        String birthdate,
        String phone_number,
        String email,
        String password
) {
}
