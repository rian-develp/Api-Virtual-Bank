package com.example.virtualbank.repositories;

import com.example.virtualbank.entities.CardEntity;
import com.example.virtualbank.projections.GetInfoAboutCardAndCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<CardEntity, String> {

    // Query que retorna apenas o número e validade do cartão, nome do banco e o nome do dono do cartão

    @Query(value = "SELECT " +
            "cd.card_number as cardNumber, " +
            "cd.validity as validity, " +
            "cd.bank_name as bankName, " +
            "cst.name as customerName " +
            "FROM cards cd " +
            "JOIN customers cst ON cd.customer_id = cst.id " +
            "WHERE cd.card_number = :number", nativeQuery = true)
    GetInfoAboutCardAndCustomer getInfoAboutCardAndCustomer(@Param("number") String cardNumber);


    //-------------------------------------------------------
    //-------------------------------------------------------


    // Query que retorna uma lista com as informações: o número e validade do cartão, nome do banco e o nome do dono do cartão

    @Query(value = "SELECT " +
            "cd.card_number as cardNumber, " +
            "cd.validity as validity, " +
            "cd.bank_name as bankName, " +
            "cst.name as customerName " +
            "FROM cards cd " +
            "JOIN customers cst ON cd.customer_id = cst.id " +
            "WHERE cd.customer_id = :customerId", nativeQuery = true)
    List<GetInfoAboutCardAndCustomer> listAllCardsByCustomerId(@Param("customerId") String customerId);
}
