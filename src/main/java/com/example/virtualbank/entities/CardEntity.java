package com.example.virtualbank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "cards")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CardEntity {

    @Id
    @Column(name = "card_number", length = 16)
    private String cardNumber;

    @Column(nullable = false)
    private LocalDate validity;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;
}