package com.example.virtualbank.dtos.card;

public record FindCustomerByEmailAndPasswordDTO(
    String email,
    String password
){}