package com.example.virtualbank.card;

import com.example.virtualbank.controllers.CardController;
import com.example.virtualbank.dtos.card.CreateCardDTO;
import com.example.virtualbank.dtos.card.GetInfoCardDTO;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.model.Card;
import com.example.virtualbank.security.SecurityFilter;
import com.example.virtualbank.services.card.CardService;
import com.example.virtualbank.util.Utils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = CardController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CardService service;

    @Test
    @DisplayName("Should return all cards of customer when customer exists")
    void shouldReturnCardListOfCustomer() throws Exception {

        String customerId = "correctId";
        GetInfoCardDTO dto = new GetInfoCardDTO("11234", Utils.convertString("24/02/1900"), "Banco do Nordeste", "Jurema");

        when(service.findAllCards(customerId)).thenReturn(List.of(dto));

        mockMvc.perform(get("/cards/{customerId}", customerId)
                        .contentType("application/json"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data[0].cardNumber").value("11234"))
                .andExpect(jsonPath("$.data[0].bankName").value("Banco do Nordeste"));

        verify(service).findAllCards(customerId);
    }

    @Test
    @DisplayName("Should return card informations when card does not exist")
    void shouldReturnCardInformationsSuccessfully() throws Exception {
        String cardNumber = "2346 6466 6464 4646";
        GetInfoCardDTO dto = new GetInfoCardDTO(cardNumber, Utils.convertString("02/03/2002"), "MasterCard", "José Vilela");
        when(service.getInfoCard(cardNumber)).thenReturn(dto);
        mockMvc.perform(get("/cards/info/{cardNumber}", cardNumber).contentType("application/json"))
                .andExpect(jsonPath("$.statusCode").value(200))
                .andExpect(jsonPath("$.data.bankName").value(dto.bankName()))
                .andExpect(jsonPath("$.data.customerName").value(dto.customerName()))
                .andExpect(jsonPath("$.data.cardNumber").value(dto.cardNumber()));

        verify(service).getInfoCard(cardNumber);
    }

    @Test
    void shouldReturnCardInformationsWhenSaved() throws Exception {
        CreateCardDTO dto = new CreateCardDTO("2246 1212 2233 2133", "06/11/2055", "Itaú", "customerId");
        when(service.createCard(any(CreateCardDTO.class))).thenReturn(new Card(dto.cardNumber(), Utils.convertString(dto.validity()), dto.bankName(), dto.customerId()));
        mockMvc.perform(post("/cards/create")
                        .contentType("application/json")
                        .content("""
                                    {
                                        "cardNumber": "%s",
                                        "validity": "%s",
                                        "bankName": "%s",
                                        "customerId": "%s"
                                    }
                                """.formatted(dto.cardNumber(), dto.validity(), dto.bankName(), dto.customerId())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data.cardNumber").value(dto.cardNumber()))
                .andExpect(jsonPath("$.data.customerId").value(dto.customerId()))
                .andDo(print());

        verify(service).createCard(any(CreateCardDTO.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer id passed by parameter")
    void shouldReturn404WhenCustomerNotExists() throws Exception {
        String customerId = "invalid-id";
        when(service.findAllCards(customerId)).thenThrow(new EntityNotFoundException("Customer not found"));
        mockMvc.perform(get("/cards/{customerId}", customerId)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.data").value("Customer not found"))
                .andDo(print());

        verify(service).findAllCards(customerId);
    }

    @Test
    void shouldReturn404WhenCardNotExists() throws Exception {
        String cardNumber = "2134 5655 6698 7878";
        when(service.getInfoCard(cardNumber)).thenThrow(new EntityNotFoundException("Card not found"));
        mockMvc.perform(get("/cards/info/{cardNumber}", cardNumber)
                        .contentType("application/json"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.data").value("Card not found"));

        verify(service).getInfoCard(cardNumber);
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not exists during createCard() method invocation")
    void shouldReturn404WhenCustomerNotExistsDuringCardCreation() throws Exception {
        String customerId = "xc0231-utr2314151555$523532f";
        CreateCardDTO dto = new CreateCardDTO("2246 1212 2233 2133", "06/11/2055", "Itaú", customerId);
        when(service.createCard(any(CreateCardDTO.class))).thenThrow(new EntityNotFoundException("Customer not found"));
        mockMvc.perform(post("/cards/create")
                        .contentType("application/json")
                        .content("""
                                    {
                                        "cardNumber": "%s",
                                        "validity": "%s",
                                        "bankName": "%s",
                                        "customerId": "%s"
                                    }        
                                """.formatted(dto.cardNumber(), dto.validity(), dto.bankName(), dto.customerId())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.data").value("Customer not found"))
                .andDo(print());
    }

    @Test
    void shouldDeleteCardSuccessfully() throws Exception {
        String cardNumber = "2443 5446 2133 0000";
        doNothing().when(service).deleteCard(cardNumber);
        mockMvc.perform(delete("/cards/delete/{cardNumber}", cardNumber)
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().string("Success in card deletion"))
                .andDo(print());
    }
}
