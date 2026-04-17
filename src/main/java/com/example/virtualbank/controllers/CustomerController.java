package com.example.virtualbank.controllers;

import com.example.virtualbank.dtos.customer.FindByEmailDTO;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.customer.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {


    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @GetMapping("/info")
    public ResponseEntity<ResponseBody<Customer>> findByEmail(@RequestBody FindByEmailDTO dto){
        Customer customer = service.findByEmail(dto.email());
        return ResponseEntity.ok(new ResponseBody<>(
                HttpStatus.OK.value(),
                customer
        ));
    }

    @GetMapping
    public ResponseEntity<ResponseBody<List<Customer>>> listAllCustomers(){
        List<Customer> customerList = service.findAllCustomers();
        return ResponseEntity.ok(new ResponseBody<>(
                HttpStatus.OK.value(),
                customerList
        ));
    }
}
