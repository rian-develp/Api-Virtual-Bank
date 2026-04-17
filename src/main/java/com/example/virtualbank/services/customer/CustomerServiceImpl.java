package com.example.virtualbank.services.customer;

import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityNotFoundException;
import com.example.virtualbank.mappers.CustomerMapper;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer findByEmail(String email){
        Optional<CustomerEntity> customerEntity = repository.findByEmail(email);
        return customerEntity.map(CustomerMapper::toCustomer)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
    }

    @Override
    public List<Customer> findAllCustomers() {
        return repository.findAll()
                .stream()
                .map(CustomerMapper::toCustomer)
                .toList();
    }
}
