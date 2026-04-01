package com.example.virtualbank.security;

import com.example.virtualbank.dtos.card.FindCustomerByEmailAndPasswordDTO;
import com.example.virtualbank.mappers.CustomerMapper;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.repositories.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CustomCustomerDetailsService implements UserDetailsService {

    private final CustomerRepository repository;

    public CustomCustomerDetailsService(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Customer customer = CustomerMapper.toCustomer(repository.findByEmail(username));

        if (customer == null){
            throw new UsernameNotFoundException("Customer not found");
        }
        return new org.springframework.security.core.userdetails.User(dto.email(), dto.password(), new ArrayList<>());
    }
}
