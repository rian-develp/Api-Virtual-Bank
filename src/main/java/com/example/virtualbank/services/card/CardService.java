package com.example.virtualbank.services.card;

import com.example.virtualbank.dtos.card.CreateCardDTO;
import com.example.virtualbank.dtos.card.GetInfoCardDTO;
import com.example.virtualbank.model.Card;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CardService {

    List<GetInfoCardDTO> findAllCards(String customerId);
    GetInfoCardDTO getInfoCard(String cardNumber);
    Card createCard(CreateCardDTO dto);
    void deleteCard(String cardNumber);
}
