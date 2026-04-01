package com.example.virtualbank.services.auth;

import com.example.virtualbank.auth.request.AuthRequestDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityAlreadyExistsException;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AuthenticationServiceImpl implements AuthenticationService {

    private final CustomerRepository repository;
    private final PasswordEncoder encoder;
    private final TokenService tokenService;

    public AuthenticationServiceImpl(CustomerRepository repository, PasswordEncoder encoder, TokenService tokenService) {
        this.repository = repository;
        this.encoder = encoder;
        this.tokenService = tokenService;
    }

    @Override
    public String login(AuthRequestDTO dto) {
        CustomerEntity customerEntity = repository.findByEmail(dto.email());

        if (customerEntity == null || !encoder.matches(dto.password(), customerEntity.getPassword())) {
            throw new EntityNotFoundException("Customer not found");
        }
        return tokenService.generateToken(customerEntity);
    }

    @Override
    public AuthResponseDTO signup(AuthRequestDTO dto) {

        CustomerEntity customerEntity = repository.findByEmail(dto.email());

        if (customerEntity != null) {
            throw new EntityAlreadyExistsException("Customer already exists");
        }

        CustomerEntity entity = new CustomerEntity(
                dto.name(),
                toLocalDate(dto.birthdate()),
                dto.phone_number(),
                dto.email(),
                encoder.encode(dto.password()));

        String token = tokenService.generateToken(entity);
        repository.save(entity);

        return new AuthResponseDTO(
                entity.getName(),
                entity.getBirthdate().toString(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                token
        );
    }


    private LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }
}
