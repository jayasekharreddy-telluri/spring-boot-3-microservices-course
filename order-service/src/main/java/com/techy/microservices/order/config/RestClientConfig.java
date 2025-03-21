package com.techy.microservices.order.config;

import com.techy.microservices.order.client.InventoryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${inventory.url}") // Read from application.properties
    private String inventoryServiceUrl;

    @Bean
    public InventoryClient restClient() {

        RestClient restClient = RestClient.builder().baseUrl(inventoryServiceUrl).build();

        var restClientAdapater = RestClientAdapter.create(restClient);

        var httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapater).build();

        return httpServiceProxyFactory.createClient(InventoryClient.class);


}
}
