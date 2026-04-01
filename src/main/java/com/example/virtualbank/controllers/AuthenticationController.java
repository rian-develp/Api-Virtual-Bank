package com.example.virtualbank.controllers;


import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.auth.request.AuthRequestDTO;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.security.TokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final CustomerRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public AuthenticationController(CustomerRepository repository, PasswordEncoder encoder, TokenService tokenService) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseBody<?>> login(@RequestBody AuthRequestDTO dto){
        CustomerEntity customerEntity = repository.findByEmail(dto.email());

        if (customerEntity == null){
            throw new EntityNotFoundException("Customer not found");
        }

        if(encoder.matches(dto.password(), customerEntity.getPassword())){
            String token = tokenService.generateToken(customerEntity);
            return ResponseEntity.ok(ResponseBody.responseBody(HttpStatus.OK.value(), token));
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseBody<?>> signup(@RequestBody AuthRequestDTO dto){
        CustomerEntity customerEntity = repository.findByEmail(dto.email());

        if (customerEntity == null){

            CustomerEntity entity = new CustomerEntity(
                    dto.name(),
                    toLocalDate(dto.birthdate()),
                    dto.phone_number(),
                    dto.email(),
                    encoder.encode(dto.password()));

            repository.save(entity);

            if (encoder.matches(dto.password(), entity.getPassword())){
                String token = tokenService.generateToken(entity);
                return ResponseEntity.ok(
                    new ResponseBody<>(
                        HttpStatus.OK.value(),
                        new AuthResponseDTO(
                            entity.getName(),
                            entity.getBirthdate().toString(),
                            entity.getPhoneNumber(),
                            entity.getEmail(),
                            token
                        )
                    )
                );
            }
        }

        return ResponseEntity.badRequest().build();
    }

    private LocalDate toLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }
}
