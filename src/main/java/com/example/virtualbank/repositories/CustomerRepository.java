package com.example.virtualbank.repositories;

import com.example.virtualbank.entities.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {

    @Query(value = "SELECT * FROM customers WHERE email = :email AND password = :password", nativeQuery = true)
    Optional<CustomerEntity> findByEmailAndPassword(@Param("email") String email, @Param("password") String password);

    Boolean existsByEmail(String email);
}
