package com.example.virtualbank.customer;

import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.services.customer.CustomerService;
import com.example.virtualbank.util.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class CustomerServiceIntegrationTests {

    @Autowired
    private CustomerService service;

    @Autowired
    private CustomerRepository repository;

    @Test
    void shouldReturnCustomerListSuccessfully() {
        List<Customer> entityList = service.findAllCustomers();
        assertThat(entityList).hasSize(3);
        assertThat(entityList)
                .extracting(Customer::getEmail)
                .contains("la@gmail.com");
    }

    @Test
    void shouldReturnCustomerSuccessfullyWhenFoundByEmail() {
        String email = "la@gmail.com";
        Customer customer = service.findByEmail(email);
        assertThat(customer).isNotNull();
        assertThat(customer.getEmail()).isEqualTo("la@gmail.com");
    }

    @Test
    void shouldThrowEntityNotFoundExceptionWhenCustomerNotFoundByEmail() {
        assertThatThrownBy(() -> service.findByEmail("inexistentEmail@gmail.com"))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Customer not found");
    }

    @BeforeEach
    void setup() {

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
