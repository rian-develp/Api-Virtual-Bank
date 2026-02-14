package com.example.virtualbank.mappers;

import com.example.virtualbank.entities.CardEntity;

public class CardMapper {

    public static com.example.virtualbank.model.Card toCard(CardEntity cardEntity){
        return new com.example.virtualbank.model.Card(
                cardEntity.getCardNumber(),
                cardEntity.getValidity(),
                cardEntity.getBankName(),
                cardEntity.getCustomer().getId());
    }
}
