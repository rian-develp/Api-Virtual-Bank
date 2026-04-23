package com.example.virtualbank.customer;

import com.example.virtualbank.controllers.CustomerController;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.security.SecurityFilter;
import com.example.virtualbank.services.customer.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = SecurityFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
public class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService service;

    @Test
    void shouldReturnCustomerWhenEmailExists() throws Exception {
        String email = "a@gmail.com";
        when(service.findByEmail(email)).thenReturn(
                new Customer(
                        UUID.randomUUID().toString(),
                        "Anderson",
                        toLocalDate("08/08/2008"),
                        "81955642133",
                        email,
                        "1234"
                ));

        mockMvc.perform(get("/customers/info")
                    .contentType("application/json")
                    .content("""
                        {
                            "email": "a@gmail.com"
                        }
                    """)
            ).andExpect(status().isOk())
             .andExpect(jsonPath("$.email").value(email))
             .andExpect(jsonPath("$.name").value("Anderson"));
    }

    @Test
    void shouldReturnAllCustomers() throws Exception {
        Customer customer = new Customer(UUID.randomUUID().toString(), "Juca", toLocalDate("02/01/1988"),
                "81955326448",
                "j@gmail.com",
                "1234");
        when(service.findAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].email").value("j@gmail.com"))
                .andExpect(jsonPath("$.data[0].name").value("Juca"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"a@gmail.com", "jurema@gmail.com", "l@gmail.com", "g@gmail.com"})
    void shouldReturn404WhenCustomerNotFound(String email) throws Exception {
        when(service.findByEmail(email)).thenThrow(new EntityNotFoundException("Customer not found"));
        mockMvc.perform(get("/customers/info")
                .contentType("application/json")
                .content("""
                    {
                        "email": "%s"
                    }
                """.formatted(email))
        ).andExpect(status().isNotFound());
    }

    private LocalDate toLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }
}
