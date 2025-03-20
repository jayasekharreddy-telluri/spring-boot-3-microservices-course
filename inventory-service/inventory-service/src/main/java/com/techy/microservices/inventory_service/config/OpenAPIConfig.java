package com.techy.microservices.inventory_service.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI inventoryServiceConfig() {
        return new OpenAPI()
                .info(new Info()
                        .title("Inventory Service API")
                        .version("v0.1")
                        .description("API documentation for the Inventory Service")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0")))
                .externalDocs(new ExternalDocumentation()
                        .description("Product Service Documentation")
                        .url("https://docs.example.com/Inventory-service"));
    }
}
