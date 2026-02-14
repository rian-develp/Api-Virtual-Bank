package com.example.virtualbank.services.card;

import com.example.virtualbank.dtos.card.CreateCardDTO;
import com.example.virtualbank.dtos.card.GetInfoCardDTO;
import com.example.virtualbank.entities.CardEntity;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.mappers.CardMapper;
import com.example.virtualbank.model.Card;
import com.example.virtualbank.repositories.CardRepository;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.util.Utils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;

    public CardServiceImpl(CardRepository cardRepository, CustomerRepository customerRepository) {
        this.cardRepository = cardRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<GetInfoCardDTO> findAllCards(String customerId) {
        var cardList = cardRepository.listAllCardsByCustomerId(customerId);

        if (cardList == null){
            throw new NullPointerException("Não existe cartões");
        }

        return cardList.stream().map(card -> new GetInfoCardDTO(
                card.getCardNumber(),
                card.getValidity(),
                card.getBankName(),
                card.getCustomerName())).toList();
    }

    @Override
    public GetInfoCardDTO getInfoCard(String cardNumber) {
        var result = cardRepository.getInfoAboutCardAndCustomer(cardNumber);
        if (result == null) {
            throw new RuntimeException("Erro");
        }
        return new GetInfoCardDTO(result.getCardNumber(), result.getValidity(), result.getBankName(), result.getCustomerName());
    }

    @Override
    public Card createCard(CreateCardDTO dto) {
        CustomerEntity customerEntity = getCustomer(dto.customerId());
        CardEntity cardEntity = cardRepository.save(new CardEntity(dto.cardNumber(), Utils.convertString(dto.validity()), dto.bankName(), customerEntity));
        return CardMapper.toCard(cardEntity);
    }

    @Override
    public void deleteCard(String cardNumber) {
        cardRepository.deleteById(cardNumber);
    }

    private CustomerEntity getCustomer(String customerId){
        Optional<CustomerEntity> customerEntity = customerRepository.findById(customerId);
        return customerEntity.orElseThrow(() -> new EntityNotFoundException("Houve um erro"));
    }
}
