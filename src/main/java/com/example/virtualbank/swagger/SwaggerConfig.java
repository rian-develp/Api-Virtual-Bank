package com.example.virtualbank.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Virtual Bank API",
        version = "1.0",
        description = "API de cadastro e login de clientes e adição de cartões de crédito usando SpringSecurity e Autenticação JWT",
        contact = @Contact(
            name = "rian-develp",
            email = "riandeveloper1@gmail.com"
        )
    )
)
public class SwaggerConfig {
}
