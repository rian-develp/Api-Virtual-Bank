package com.example.virtualbank.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private String cardNumber;
    private LocalDate validity;
    private String bankName;
    private String customerId;
}
