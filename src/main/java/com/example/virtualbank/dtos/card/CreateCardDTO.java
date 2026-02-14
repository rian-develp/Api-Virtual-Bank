package com.example.virtualbank.dtos.card;

public record CreateCardDTO(
    String cardNumber,
    String validity,
    String bankName,
    String customerId
) {
}
