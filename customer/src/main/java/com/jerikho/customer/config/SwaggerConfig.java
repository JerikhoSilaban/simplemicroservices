package com.jerikho.customer.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("customer")
                .pathsToMatch("/api/v1/customers/**") // Pastikan ini sesuai dengan controller Anda
                .build();
    }

    @Bean
    OpenAPI customersApi() {
        return new OpenAPI()
                .info(new Info().title("Customer API").version("1.0"));
    }
}
