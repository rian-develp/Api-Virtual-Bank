package com.example.virtualbank.services.auth;

import com.example.virtualbank.auth.request.AuthRequestLoginDTO;
import com.example.virtualbank.auth.request.AuthRequestSignUpDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityAlreadyExistsException;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.security.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
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
    public String login(AuthRequestLoginDTO dto) {
        return repository.findByEmail(dto.email())
                .filter(c -> encoder.matches(dto.password(), c.getPassword()))
                .map(tokenService::generateToken)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    @Override
    public AuthResponseDTO signup(AuthRequestSignUpDTO dto) {

        repository.findByEmail(dto.email())
                .ifPresent(customer -> {
                    throw new EntityAlreadyExistsException("Customer already exists");
                });


        CustomerEntity entity = new CustomerEntity(
                dto.name(),
                toLocalDate(dto.birthdate()),
                dto.phone_number(),
                dto.email(),
                encoder.encode(dto.password()));

        repository.save(entity);
        String token = tokenService.generateToken(entity);

        return new AuthResponseDTO(
                entity.getId(),
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
