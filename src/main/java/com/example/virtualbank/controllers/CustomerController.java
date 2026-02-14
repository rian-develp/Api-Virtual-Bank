package com.example.virtualbank.controllers;

import com.example.virtualbank.dtos.card.FindCustomerByEmailAndPasswordDTO;
import com.example.virtualbank.dtos.customer.CreateCustomerDTO;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.services.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {


    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginCustomer(@RequestBody FindCustomerByEmailAndPasswordDTO dto){
        Customer customer = service.doLogin(dto);
        return ResponseEntity.ok(customer);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpCustomer(@RequestBody CreateCustomerDTO dto){
        Customer customer = service.doSignUp(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }
    
}
