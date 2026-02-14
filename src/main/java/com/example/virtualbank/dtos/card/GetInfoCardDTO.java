package com.example.virtualbank.dtos.card;

import java.time.LocalDate;

public record GetInfoCardDTO(
        String cardNumber,
        LocalDate validity,
        String bankName,
        String customerName
) {
}
