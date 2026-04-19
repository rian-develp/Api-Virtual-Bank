package com.example.virtualbank.controllers;

import com.example.virtualbank.dtos.card.CreateCardDTO;
import com.example.virtualbank.dtos.card.GetInfoCardDTO;
import com.example.virtualbank.model.Card;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.card.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/cards")
public class CardController {

    private final CardService service;

    public CardController(CardService service) {
        this.service = service;
    }

    @Operation(
            summary = "Lists all cards from specific user",
            description = "Lists all cards that user id is in url"
    )
    @ApiResponse(responseCode = "200", description = "Success")
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


    @Operation(
            summary = "Shows specific card data",
            description = "Shows card data that card number in by url"
    )
    @ApiResponse(responseCode = "200", description = "Success")
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

    @Operation(
            summary = "Save card",
            description = "Save credit card informations in database"
    )
    @ApiResponse(responseCode = "201", description = "Sucesso")
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

    @Operation(
            summary = "Deletes specific card",
            description = "Deletes from database the credit card number in url"
    )
    @ApiResponse(responseCode = "200", description = "Success")
    @DeleteMapping("/delete/{cardNumber}")
    public ResponseEntity<String> deleteCard(@PathVariable("cardNumber") String cardNumber){
        service.deleteCard(cardNumber);
        return ResponseEntity.ok("Success in card deletion");
    }
}
