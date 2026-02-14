package com.example.virtualbank.dtos.customer;

public record CreateCustomerDTO(
    String name,
    String birthdate,
    String phone_number,
    String email,
    String password
){}
