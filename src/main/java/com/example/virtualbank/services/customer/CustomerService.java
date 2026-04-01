package com.example.virtualbank.services.customer;

import com.example.virtualbank.dtos.customer.CreateCustomerDTO;
import com.example.virtualbank.dtos.card.FindCustomerByEmailAndPasswordDTO;
import com.example.virtualbank.model.Customer;

public interface CustomerService {

    Customer doLogin(FindCustomerByEmailAndPasswordDTO dto);
    Customer doSignUp(CreateCustomerDTO dto);
    Customer findByEmail(String email);
    Boolean existsByEmail(String email);
}
