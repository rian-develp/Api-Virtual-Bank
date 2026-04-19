package com.example.virtualbank.services.customer;

import com.example.virtualbank.model.Customer;
import java.util.List;

public interface CustomerService {

    Customer findByEmail(String email);
    List<Customer> findAllCustomers();
}
