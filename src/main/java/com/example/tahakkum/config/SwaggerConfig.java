package com.example.tahakkum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        System.err.println("Swagger initialized!");
        return new OpenAPI()
            .info(new Info()
            .title("Takhakkum API Documentation")
            .version("1.0")
            .description("Thakkum is basic resource manegement and rich authentication RESTful API."));
    }
}

