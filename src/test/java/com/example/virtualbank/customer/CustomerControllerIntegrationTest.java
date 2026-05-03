package com.example.virtualbank.customer;

import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository repository;

    @Test
    void shouldReturn200AndAnListFromCustomers() throws Exception {
        mockMvc.perform(get("/customers").contentType("application/json"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data").isNotEmpty())
                .andDo(print());
    }

    @Test
    void shouldReturn200AndCustomerDataWhenFoundByEmail() throws Exception {
        mockMvc.perform(get("/customers/info")
                .contentType("application/json")
                .content("""
                    {
                        "email": "la@gmail.com"
                    }
                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Lucas Almeida"))
                .andExpect(jsonPath("$.phoneNumber").value("81985661234"))
                .andExpect(jsonPath("$.email").value("la@gmail.com"))
                .andDo(print());
    }

    @Test
    void should404WhenCustomerNotFoundByEmail() throws Exception {
        mockMvc.perform(get("/customers/info")
                .contentType("application/json")
                .content("""
                    {
                        "email": "emailnotexist@gmail.com"
                    }
                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404))
                .andExpect(jsonPath("$.data").value("Customer not found"))
                .andDo(print());
    }


    @BeforeEach
    void setup(){
        CustomerEntity entity1 = new CustomerEntity(
                "Joana Bezerra",
                Utils.convertString("02/05/2000"),
                "8199663211",
                "jb@gmail.com",
                "1234");

        CustomerEntity entity2 = new CustomerEntity(
                "Lucas Almeida",
                Utils.convertString("03/03/2009"),
                "81985661234",
                "la@gmail.com",
                "1234");

        CustomerEntity entity3 = new CustomerEntity(
                "Andersen Luke",
                Utils.convertString("05/06/1999"),
                "81988794444",
                "al@gmail.com",
                "1234");

        repository.save(entity1);
        repository.save(entity2);
        repository.save(entity3);
    }
}
