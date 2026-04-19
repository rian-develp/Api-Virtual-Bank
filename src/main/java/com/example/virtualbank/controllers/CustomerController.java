package com.example.virtualbank.controllers;

import com.example.virtualbank.dtos.customer.FindByEmailDTO;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.responsebody.ResponseBody;
import com.example.virtualbank.services.customer.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/customers")
public class CustomerController {


    private final CustomerService service;

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    @Operation(
            summary = "Find user",
            description = "Find user through email passed in request body"
    )
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping("/info")
    public ResponseEntity<Customer> findByEmail(@RequestBody FindByEmailDTO dto){
        Customer customer = service.findByEmail(dto.email());
        return ResponseEntity.ok(customer);
    }

    @Operation(
            summary = "Lists users",
            description = "Lists all users in database"
    )
    @ApiResponse(responseCode = "200", description = "Success")
    @GetMapping
    public ResponseEntity<ResponseBody<List<Customer>>> listAllCustomers(){
        List<Customer> customerList = service.findAllCustomers();
        return ResponseEntity.ok(new ResponseBody<>(
                HttpStatus.OK.value(),
                customerList
        ));
    }
}
