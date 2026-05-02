package com.example.virtualbank.authentication;

import com.example.virtualbank.repositories.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;


    @Test
    void shouldSignUpSuccessfully() throws Exception {
        mockMvc.perform(post("/auth/signup")
                .contentType("application/json")
                .content("""
                        {
                          "name": "Joana Bezerra",
                          "birthdate": "14/07/1985",
                          "phone_number": "81995441122",
                          "email": "jb@gmail.com",
                          "password": "1234"
                        }
                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andExpect(jsonPath("$.statusCode").value(201));

        assertThat(repository.findByEmail("jb@gmail.com")).isPresent()
                .hasValueSatisfying(entity -> assertThat(entity.getEmail()).isEqualTo("jb@gmail.com"));
    }

    @Test
    void shouldDoLoginSuccessfully() throws Exception {

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content("""
                        {
                          "name": "Leila Grasielle",
                          "birthdate": "14/07/1985",
                          "phone_number": "81995441122",
                          "email": "lg@gmail.com",
                          "password": "1234"
                        }
                """));

        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                    {
                        "email": "lg@gmail.com",
                        "password": "1234"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldReturn404WhenCustomerNotFound() throws Exception {
        mockMvc.perform(post("/auth/login")
                .contentType("application/json")
                .content("""
                    {
                        "email": "emailnotexists404@gmail.com",
                        "password": "zxc123" 
                    }
                """)).andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data").value("Customer not found"))
                .andDo(print());
    }

    @Test
    void shouldReturn400WhenCustomerAlreadyExists() throws Exception {
        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content("""
                        {
                          "name": "Joana Bezerra",
                          "birthdate": "14/07/1985",
                          "phone_number": "81995441122",
                          "email": "jb@gmail.com",
                          "password": "1234"
                        }
                """));

        mockMvc.perform(post("/auth/signup")
                        .contentType("application/json")
                        .content("""
                        {
                          "name": "Joana Bezerra",
                          "birthdate": "14/07/1985",
                          "phone_number": "81995441122",
                          "email": "jb@gmail.com",
                          "password": "1234"
                        }
                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.statusCode").value(400))
                .andExpect(jsonPath("$.data").value("Customer already exists"))
                .andDo(print());
    }
}
