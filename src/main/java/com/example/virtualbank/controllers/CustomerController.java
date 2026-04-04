package com.example.virtualbank.controllers;

import com.example.virtualbank.model.Customer;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {


    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/info/{email}")
    public ResponseEntity<ResponseBody<Customer>> findByEmail(@PathVariable("email") String email){
        Customer customer = service.findByEmail(email);
        return ResponseEntity.ok(new ResponseBody<>(
                HttpStatus.OK.value(),
                customer
        ));
    }

    @GetMapping("/info/{email}/exists")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable("email") String email){
        boolean result = service.existsByEmail(email);
        return ResponseEntity.ok(result);
    }

}
