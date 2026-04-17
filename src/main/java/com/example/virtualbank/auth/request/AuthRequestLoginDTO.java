package com.example.virtualbank.auth.request;

public record AuthRequestLoginDTO (
        String email,
        String password
) {
}
