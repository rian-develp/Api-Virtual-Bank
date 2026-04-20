package com.example.virtualbank.authentication;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AuthenticationRepositoryTest {

    @Test
    void test(){
        assertThat(2);
    }
}
