package com.example.virtualbank.card;

import com.example.virtualbank.dtos.card.CreateCardDTO;
import com.example.virtualbank.dtos.card.GetInfoCardDTO;
import com.example.virtualbank.entities.CardEntity;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.projections.GetInfoAboutCardAndCustomer;
import com.example.virtualbank.repositories.CardRepository;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.services.card.CardServiceImpl;
import com.example.virtualbank.util.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository repository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CardServiceImpl service;

    @ParameterizedTest
    @ValueSource(strings = {"q123", "cxcc213213", "d234-dff3453242321-sxczc", "lpp-2lk12-3244440494940"})
    void shouldReturnAllCards(String id){
        GetInfoAboutCardAndCustomer cardInfo = mock(GetInfoAboutCardAndCustomer.class);
        when(cardInfo.getCardNumber()).thenReturn("1234-5678");
        when(cardInfo.getBankName()).thenReturn("VISA");
        when(repository.listAllCardsByCustomerId(id))
                .thenReturn(List.of(cardInfo));

        List<GetInfoCardDTO> cardInfoList = service.findAllCards(id);
        assertThat(cardInfoList).hasSize(1);
        assertThat(cardInfoList.get(0).cardNumber()).isEqualTo("1234-5678");
        assertThat(cardInfoList.get(0).bankName()).isEqualTo("VISA");
    }

    @Test
    void shouldDeleteCardSuccessfully(){
        service.deleteCard(any());
        verify(repository).deleteById(any());
    }

    @Test
    void shouldSaveCardSuccessfully(){
        String cardNumber = "1234 8888 9999 1022";
        CustomerEntity customerEntity = new CustomerEntity("Manuel Gomes", Utils.convertString("20/01/1989"), "81988663300", "mg@gmail.com", "1234");
        when(customerRepository.findById(customerEntity.getId())).thenReturn(Optional.of(customerEntity));
        when(repository.save(any(CardEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));
        service.createCard(new CreateCardDTO(cardNumber, "20/05/2030", "Itaú", customerEntity.getId()));
        verify(repository).save(argThat(card -> card.getCardNumber().equals(cardNumber) && card.getBankName().equals("Itaú")));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found during CreateCard() method")
    void shouldThrowExceptionWhenCustomerNotExists(){
        String customerId = "invalid-id";
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());
        assertThatThrownBy(() ->
                service.createCard(new CreateCardDTO(
                    "1234",
                    "20/02/2066",
                    "Z1",
                    customerId)
                )
        ).isInstanceOf(EntityNotFoundException.class);

        verify(customerRepository).findById(customerId);
        verify(repository, never()).save(any());
    }
}