package com.javaacademy.CryptoWallet.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("Проект \"Криптокошелек\"")
                .contact(new Contact().name("Чернов Андрей").email("che34@mail.ru"));
        return new OpenAPI().info(info);
    }
}
