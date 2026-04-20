package com.example.virtualbank.authentication;

import com.example.virtualbank.auth.request.AuthRequestLoginDTO;
import com.example.virtualbank.auth.request.AuthRequestSignUpDTO;
import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.security.TokenService;
import com.example.virtualbank.services.auth.AuthenticationServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private CustomerRepository repository;

    @Mock
    private PasswordEncoder encoder;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationServiceImpl service;

    @Test
    @DisplayName("Should do login successfully")
    void shouldLoginSuccessfully(){

        AuthRequestLoginDTO loginDTO = new AuthRequestLoginDTO("lg@gmail.com", "1234");
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setPassword("encodedPassword");

        when(repository.findByEmail(loginDTO.email()))
                .thenReturn(Optional.of(customerEntity));

        when(encoder.matches("1234", customerEntity.getPassword()))
                .thenReturn(true);

        when(tokenService.generateToken(customerEntity))
                .thenReturn("token");

        String token = service.login(loginDTO);
        assertThat(token).isEqualTo("token");
    }

    @ParameterizedTest
    @DisplayName("Should do login successfully")
    @ValueSource(strings = {"sd@gmail.com", "2dsd2@gmail.com", "cxc@gmail.com", "lks@gmail.com", "fg@domain.gmail.com.br"})
    void shouldLoginSuccessfullyForMultiplesEmails(String email){

        AuthRequestLoginDTO loginDTO = new AuthRequestLoginDTO(email, "1xcwe24");
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setEmail(email);
        customerEntity.setPassword("encodedPassword");

        when(repository.findByEmail(loginDTO.email()))
                .thenReturn(Optional.of(customerEntity));

        when(encoder.matches("1xcwe24", customerEntity.getPassword()))
                .thenReturn(true);

        when(tokenService.generateToken(customerEntity))
                .thenReturn("token");

        String token = service.login(loginDTO);
        assertThat(token).isEqualTo("token");
    }

    @Test
    @DisplayName("Should does signup successfully")
    void shouldSignUpSuccessfully(){
        AuthRequestSignUpDTO signUpDTO = new AuthRequestSignUpDTO(
                "Leila Grasielle",
                "08/09/2002",
                "81982321131",
                "lg@gmail.com",
                "1234"
        );

        when(repository.findByEmail(signUpDTO.email())).thenReturn(Optional.empty());
        when(encoder.encode("1234")).thenReturn("encoded");
        when(tokenService.generateToken(any())).thenReturn("token");

        AuthResponseDTO response = service.signup(signUpDTO);
        assertThat(response.email()).isEqualTo("lg@gmail.com");
        verify(repository).save(any(CustomerEntity.class));
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not found")
    void shouldThrowEntityNotFoundExceptionWhenCustomerNotFound(){
        AuthRequestLoginDTO loginDTO = new AuthRequestLoginDTO("as23@gmail.com.br", "1234");

        when(repository.findByEmail(loginDTO.email())).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.login(loginDTO))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found");
    }
}
