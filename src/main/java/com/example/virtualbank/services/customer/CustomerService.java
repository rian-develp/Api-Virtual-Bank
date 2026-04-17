package com.example.virtualbank.services.customer;

import com.example.virtualbank.model.Customer;

public interface CustomerService {

    Customer findByEmail(String email);
}
