package com.example.virtualbank.card;

import com.example.virtualbank.entities.CardEntity;
import com.example.virtualbank.entities.CustomerEntity;
import com.example.virtualbank.projections.GetInfoAboutCardAndCustomer;
import com.example.virtualbank.repositories.CardRepository;
import com.example.virtualbank.util.Utils;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class CardRepositoryTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private CardRepository repository;

    @Test
    void shouldReturnCardListSuccessfully(){
        Optional<CardEntity> optional = repository.findById("1123222255554444");
        List<GetInfoAboutCardAndCustomer> cardList = repository.listAllCardsByCustomerId(optional.get().getCustomer().getId());
        assertThat(cardList).hasSize(2);
        assertThat(cardList.get(0).getCustomerName()).isEqualTo("Karina Silva");
    }

    @Test
    void shouldReturnCardInfoSuccessfully(){
        GetInfoAboutCardAndCustomer getInfoAboutCardAndCustomer = repository.getInfoAboutCardAndCustomer("1025299257754876");
        assertThat(getInfoAboutCardAndCustomer.getCustomerName()).isEqualTo("Michael Castro");
        assertThat(getInfoAboutCardAndCustomer.getBankName()).isEqualTo("Banco do Nordeste");
    }

    @Test
    void shouldPersistCardSuccessfully(){
        Optional<CardEntity> optional = repository.findById("1123222255554444");

        CardEntity cardEntity = new CardEntity(
                "0055200114556899",
                Utils.convertString("05/06/2033"),
                "Banco do Brasil",
                optional.get().getCustomer());

        repository.save(cardEntity);

        assertThat(repository.getInfoAboutCardAndCustomer("0055200114556899").getBankName()).isEqualTo("Banco do Brasil");
        assertThat(repository.getInfoAboutCardAndCustomer("0055200114556899").getCustomerName()).isEqualTo("Karina Silva");

    }

    @Test
    void shouldReturnEmptyOptionalWhenCardNotFound(){
        Optional<CardEntity> cardEntity = repository.findById("0100100000010010");
        assertThat(cardEntity).isEmpty();
    }

    @BeforeEach
    void setup() {

        CustomerEntity customer1 = new CustomerEntity(
                "Karina Silva",
                Utils.convertString("05/09/2003"),
                "81988641233",
                "ks@gmail.com",
                "e$213-dxcc34");

        CustomerEntity customer2 = new CustomerEntity(
                "Michael Castro",
                Utils.convertString("08/09/2003"),
                "81988641453",
                "mc@gmail.com",
                "f$sacxz-sdq244qqq");


        entityManager.persist(customer1);
        entityManager.persist(customer2);

        CardEntity card1 = new CardEntity();
        card1.setCardNumber("1123222255554444");
        card1.setValidity(Utils.convertString("05/12/2029"));
        card1.setBankName("Itaú");
        card1.setCustomer(customer1);


        CardEntity card2 = new CardEntity();
        card2.setCardNumber("5602121259554321");
        card2.setValidity(Utils.convertString("05/12/2029"));
        card2.setBankName("Bradesco");
        card2.setCustomer(customer1);


        CardEntity card3 = new CardEntity();
        card3.setCardNumber("1023222255554988");
        card3.setValidity(Utils.convertString("05/05/2028"));
        card3.setBankName("Itaú");
        card3.setCustomer(customer2);


        CardEntity card4 = new CardEntity();
        card4.setCardNumber("1001745202504008");
        card4.setValidity(Utils.convertString("05/05/2030"));
        card4.setBankName("Santander");
        card4.setCustomer(customer2);


        CardEntity card5 = new CardEntity();
        card5.setCardNumber("1025299257754876");
        card5.setValidity(Utils.convertString("05/05/2030"));
        card5.setBankName("Banco do Nordeste");
        card5.setCustomer(customer2);

        entityManager.persist(card1);
        entityManager.persist(card2);
        entityManager.persist(card3);
        entityManager.persist(card4);
        entityManager.persist(card5);
    }
}
