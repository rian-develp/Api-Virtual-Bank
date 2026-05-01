package com.example.virtualbank.customer;

import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.util.Utils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CustomerRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CustomerRepository repository;

    @Test
    void shouldReturnListFromCustomers(){
        List<CustomerEntity> entityList = repository.findAll();
        assertThat(entityList).hasSize(3)
                .extracting(CustomerEntity::getEmail)
                .containsExactly("ks@gmail.com", "mc@gmail.com", "ms@gmail.com");
    }

    @Test
    void shouldReturnAOptionalOfCustomerSuccessfully(){
        String email = "mc@gmail.com";
        Optional<CustomerEntity> optional = repository.findByEmail(email);
        assertThat(optional).isPresent()
                .get()
                .extracting(CustomerEntity::getEmail)
                .isEqualTo(email);
    }

    @Test
    void shouldPersistACustomerInDb(){
        CustomerEntity entity = new CustomerEntity("Marcus Ricardo", Utils.convertString("03/03/2000"), "81994559877", "mr@gmail.com", "1234");
        repository.save(entity);
        Boolean result = repository.findAll().stream().anyMatch(entity1 -> entity1.getEmail().equals("mr@gmail.com"));
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnEmptyOptionalWhenCustomerFoundByEmailNotExists(){
        String email = "email@notfound";
        Optional<CustomerEntity> optional = repository.findByEmail(email);
        assertThat(optional).isEmpty();
    }


    @BeforeEach
    void insertDataInDb(){
        List<CustomerEntity> entityList = List.of(
                new CustomerEntity("Karina Silva", Utils.convertString("05/09/2003"), "81988641233", "ks@gmail.com", "e$213-dxcc34"),
                new CustomerEntity("Michael Castro", Utils.convertString("08/09/2003"), "81988641453", "mc@gmail.com", "f$sacxz-sdq244qqq"),
                new CustomerEntity("Mallorca Silveira", Utils.convertString("15/10/1993"), "81980041233", "ms@gmail.com", "fw@fasf$-cxzcxv")
        );

        entityList.forEach(customer -> entityManager.persist(customer));
        entityManager.flush();
    }
}
