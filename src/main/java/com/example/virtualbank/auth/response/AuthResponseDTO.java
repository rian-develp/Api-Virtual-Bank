package com.example.virtualbank.auth.response;

public record AuthResponseDTO (
    String id,
    String name,
    String birthdate,
    String phone_number,
    String email,
    String token
){
}
