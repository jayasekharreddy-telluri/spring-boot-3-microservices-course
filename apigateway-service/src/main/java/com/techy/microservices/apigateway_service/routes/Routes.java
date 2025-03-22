package com.techy.microservices.apigateway_service.routes;

import org.springframework.cloud.gateway.server.mvc.filter.CircuitBreakerFilterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.RequestPredicate;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {
    @Bean
    public RouterFunction<ServerResponse> productServiceRoute(){

        return route("product_service")
                .route(RequestPredicates.path("/api/product"), HandlerFunctions.
                        http("http://localhost:8080")).
                 filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                URI.create("forward:/fallbackRoute"))).build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRoute(){

        return route("order_service")
                .route(RequestPredicates.path("/api/orders"), HandlerFunctions.
                        http("http://localhost:8081")) .filter(CircuitBreakerFilterFunctions.circuitBreaker("orderServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute"))).build();

    }
    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRoute(){

        return route("inventory_service")
                .route(RequestPredicates.path("/api/inventory"), HandlerFunctions.
                        http("http://localhost:8082")) .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventoryServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute"))).build();

    }

    //swagger routing config

    @Bean
    public RouterFunction<ServerResponse> productServiceRouteOfSwagger(){

        return route("product_service_swagger")
                .route(RequestPredicates.path("/aggregate/product-service/v3/api-docs"),
                        HandlerFunctions.
                        http("http://localhost:8080")).
                        filter(setPath("/api-docs")) .filter(CircuitBreakerFilterFunctions.circuitBreaker("productServiceCircuitBreaker",
                        URI.create("forward:/fallbackRoute"))).
                         build();

    }

    @Bean
    public RouterFunction<ServerResponse> orderServiceRouteOfSwagger(){

        return route("order_service_swagger")
                .route(RequestPredicates.path("/aggregate/order-service/v3/api-docs"),
                        HandlerFunctions.
                                http("http://localhost:8081")).
                filter(setPath("/api-docs")) .filter(CircuitBreakerFilterFunctions.circuitBreaker("order_service_swagger_CircuitBreaker",
                        URI.create("forward:/fallbackRoute"))).
                build();

    }

    @Bean
    public RouterFunction<ServerResponse> inventoryServiceRouteOfSwagger(){

        return route("inventory_service_swagger")
                .route(RequestPredicates.path("/aggregate/inventory-service/v3/api-docs"),
                        HandlerFunctions.
                                http("http://localhost:8082")).
                filter(setPath("/api-docs")) .filter(CircuitBreakerFilterFunctions.circuitBreaker("inventory_service_swagger_CircuitBreaker",
                        URI.create("forward:/fallbackRoute"))).
                build();

    }

    //fallback

    @Bean
    public RouterFunction<ServerResponse> fallbackRoute() {
        return route("fallbackRoute")
                .GET("/fallbackRoute", request -> ServerResponse.status(HttpStatus.SERVICE_UNAVAILABLE)
                        .body("Service Unavailable, please try again later"))
                .build();
    }


}
