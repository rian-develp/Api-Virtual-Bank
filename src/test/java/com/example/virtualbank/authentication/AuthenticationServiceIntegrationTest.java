package com.example.virtualbank.authentication;

import com.example.virtualbank.auth.request.AuthRequestLoginDTO;
import com.example.virtualbank.auth.request.AuthRequestSignUpDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.exceptions.EntityAlreadyExistsException;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.services.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class AuthenticationServiceIntegrationTest {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private CustomerRepository repository;


    @Test
    void shouldDoSignUpSuccessfully(){
        AuthRequestSignUpDTO signUpDTO = new AuthRequestSignUpDTO(
                "Wanderson Costa",
                "08/09/2001",
                "81982315161",
                "wc@gmail.com",
                "1234"
        );

        AuthResponseDTO responseDTO = service.signup(signUpDTO);
        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.token()).isNotBlank();
        assertThat(repository.findByEmail("wc@gmail.com")).isPresent();
        assertThat(repository.findByEmail("wc@gmail.com").get().getEmail()).isEqualTo("wc@gmail.com");
    }

    @Test
    void shouldDoLoginSuccessfully(){
        AuthRequestLoginDTO dto = new AuthRequestLoginDTO("lg@gmail.com", "1234");
        String token = service.login(dto);
        assertThat(token).isNotBlank();
    }

    @Test
    void shouldThrowEntityAlreadyExistsExceptionDuringSignUp(){
        AuthRequestSignUpDTO signUpDTO = new AuthRequestSignUpDTO(
                "Leila Grasielle",
                "08/09/2001",
                "81982315161",
                "lg@gmail.com",
                "1234"
        );

        assertThatThrownBy(() -> service.signup(signUpDTO))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessage("Customer already exists");
    }

    @Test
    void shouldThrowEntityNotFoundExceptionDuringLogin(){
        AuthRequestLoginDTO dto = new AuthRequestLoginDTO("wc@gmail.com", "1234");
        assertThatThrownBy(() -> service.login(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found");
    }

    @BeforeEach
    void setup(){

        AuthRequestSignUpDTO signUpDTO = new AuthRequestSignUpDTO(
                "Leila Grasielle",
                "08/09/2001",
                "81982315161",
                "lg@gmail.com",
                "1234"
        );

        service.signup(signUpDTO);
    }
}