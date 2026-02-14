package com.example.virtualbank.mappers;

import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.model.Customer;

public class CustomerMapper {

    public static Customer toCustomer(CustomerEntity entity){
        return new Customer(
                entity.getId(),
                entity.getName(),
                entity.getBirthdate(),
                entity.getPhoneNumber(),
                entity.getEmail(),
                entity.getPassword()
        );
    }
}
