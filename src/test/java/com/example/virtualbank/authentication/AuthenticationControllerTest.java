package com.example.virtualbank.authentication;

import com.example.virtualbank.auth.response.AuthResponseDTO;
import com.example.virtualbank.controllers.AuthenticationController;
import com.example.virtualbank.exceptions.EntityAlreadyExistsException;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.security.SecurityFilter;
import com.example.virtualbank.services.auth.AuthenticationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthenticationController.class,
            excludeFilters = @ComponentScan.Filter(
                    type = FilterType.ASSIGNABLE_TYPE,
                    classes = SecurityFilter.class
            )
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService service;


    // When Login is Success
    @Test
    void shouldLoginAndReturnToken() throws Exception {
        when(service.login(any())).thenReturn("token");

        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                    {
                        "email": "lg@gmail.com",
                        "password": "sca"
                    }
                """)
        ).andExpect(status().isOk())
            .andExpect(jsonPath("$.data").value("token"));
    }

    //When customer not exists
    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        when(service.login(any())).thenThrow(new EntityNotFoundException("Customer not found"));

        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                    {
                        "email": "23d@gmail.com",
                        "password": "45523"
                    }
                """)
                ).andExpect(status().isNotFound());
    }

    // When signup is success
    @Test
    void shouldSignupSuccessfully() throws Exception {
        when(service.signup(any())).thenReturn(new AuthResponseDTO(
                UUID.randomUUID().toString(),
                "Joana Bezerra",
                "14/07/1985",
                "81995441122",
                "jb@gmail.com",
                "token"
        ));

        mockMvc.perform(
            post("/auth/signup")
            .contentType("application/json")
            .content("""
                {
                    "name": "Joana Bezerra",
                    "birthdate": "14/07/1985",
                    "phone_number": "81995441122",
                    "email": "jb@gmail.com",
                    "password": "1234"
                }
            """)
        ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.email").value("jb@gmail.com"))
                .andExpect(jsonPath("$.data.token").value("token"))
                .andExpect(jsonPath("$.statusCode").value(201));
    }

    // When customer already exists in database
    @Test
    void shouldReturn400WhenCustomerAlreadyExists() throws Exception {
        when(service.signup(any())).thenThrow(new EntityAlreadyExistsException("Customer already exists"));

        mockMvc.perform(post("/auth/signup")
                .contentType("application/json")
                .content("""
                            "Joana Bezerra",
                            "birthdate": "14/07/1985",
                            "phone_number": "81995441122",
                            "email": "jb@gmail.com",
                            "password": "1234"
                        """)
        ).andExpect(status().isBadRequest())
         .andExpect(jsonPath("$.data").value("Customer already exists"));
    }
}
