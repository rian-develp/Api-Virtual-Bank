package com.example.virtualbank.services.customer;

import com.example.virtualbank.dtos.card.FindCustomerByEmailAndPasswordDTO;
import com.example.virtualbank.dtos.customer.CreateCustomerDTO;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.exceptions.EntityAlreadyExistsException;
import com.example.virtualbank.exceptions.UnauthorizedException;
import com.example.virtualbank.mappers.CustomerMapper;
import com.example.virtualbank.model.Customer;
import com.example.virtualbank.repositories.CustomerRepository;
import com.example.virtualbank.util.Utils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository repository;

    public CustomerServiceImpl(CustomerRepository repository) {
        this.repository = repository;
    }

    @Override
    public Customer doLogin(FindCustomerByEmailAndPasswordDTO dto) {
        Optional<CustomerEntity> entity = repository.findByEmailAndPassword(dto.email(), dto.password());
        CustomerEntity customerEntity = entity.orElseThrow(() -> new UnauthorizedException("Email or password incorrect"));
        return CustomerMapper.toCustomer(customerEntity);
    }

    @Override
    public Customer doSignUp(CreateCustomerDTO dto){

        if (existsByEmail(dto.email())){
            throw new EntityAlreadyExistsException("Customer already exists");
        }

        String phoneNumberFormated = Utils.removeMasksFromPhoneNumber(dto.phone_number());

        CustomerEntity entity = repository.save(new CustomerEntity(
                dto.name(),
                Utils.convertString(dto.birthdate()),
                phoneNumberFormated,
                dto.email(),
                dto.password()));

        return CustomerMapper.toCustomer(entity);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
