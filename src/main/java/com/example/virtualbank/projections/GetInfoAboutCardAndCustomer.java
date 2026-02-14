package com.example.virtualbank.projections;

import java.time.LocalDate;

// Projection para resultado da consulta com join no CardRepository

public interface GetInfoAboutCardAndCustomer {

    String getCardNumber();
    LocalDate getValidity();
    String getBankName();
    String getCustomerName();

}
