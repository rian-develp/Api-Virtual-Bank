package com.example.virtualbank.controllers;

import com.example.virtualbank.dtos.card.CreateCardDTO;
import com.example.virtualbank.dtos.card.GetInfoCardDTO;
import com.example.virtualbank.model.Card;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.card.CardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<ResponseBody<List<GetInfoCardDTO>>> listAllCards(@PathVariable("customerId") String customerId){
        List<GetInfoCardDTO> cardList = service.findAllCards(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseBody.responseBody(
                        HttpStatus.OK.value(),
                        cardList
                )
        );
    }

    @GetMapping("/info/{cardNumber}")
    public ResponseEntity<ResponseBody<GetInfoCardDTO>> getInfoCardDTO(@PathVariable("cardNumber") String cardNumber){
        var result = service.getInfoCard(cardNumber);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseBody.responseBody(
                        HttpStatus.OK.value(),
                        result
                )
        );
    }

    @PostMapping("/create")
    public ResponseEntity<ResponseBody<Card>> createCard(@RequestBody CreateCardDTO dto){
        Card card = service.createCard(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ResponseBody.responseBody(
                        HttpStatus.CREATED.value(),
                        card
                )
        );
    }

    @DeleteMapping("/delete/{cardNumber}")
    public ResponseEntity<String> deleteCard(@PathVariable("cardNumber") String cardNumber){
        service.deleteCard(cardNumber);
        return ResponseEntity.ok("Success in card deletion");
    }
}
