package com.example.virtualbank.customer;

import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.services.customer.CustomerServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceTest {

    @Mock
    private CustomerRepository repository;

    @InjectMocks
    private CustomerServiceImpl service;


    @Test
    void shouldReturnCustomerList(){
        CustomerEntity entity1 = new CustomerEntity("Luan", toLocalDate("22/02/2000"), "81522334566", "l@gmail.com", "1234");
        CustomerEntity entity2 = new CustomerEntity("Ruan", toLocalDate("12/01/2010"), "81522339966", "r@gmail.com", "1234");

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<Customer> result = service.findAllCustomers();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getEmail()).isEqualTo("l@gmail.com");
    }

    @Test
    @DisplayName("Should return customer when findByEmail method is invoked")
    void shouldReturnCustomer(){
        CustomerEntity entity = new CustomerEntity("Luan", toLocalDate("20/02/2000"), "81988664422", "l@gmail.com", "1234");
        when(repository.findByEmail("l@gmail.com")).thenReturn(Optional.of(entity));

        Customer customer = service.findByEmail("l@gmail.com");
        assertThat(customer.getEmail()).isEqualTo(entity.getEmail());
    }

    @Test
    @DisplayName("Should throw EntityNotFoundException when customer not exists")
    void shouldReturn404WhenCustomerNotExists(){
        String email = "l@gmail.com";
        when(repository.findByEmail("l@gmail.com")).thenReturn(Optional.empty());
        assertThatThrownBy(() -> service.findByEmail(email))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Customer not found");
    }

    private LocalDate toLocalDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }
}
