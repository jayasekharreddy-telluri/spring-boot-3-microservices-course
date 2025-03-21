package com.techy.microservices.apigateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private static final String[] PUBLIC_URLS = {
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/api/docs/**",
            "/webjars/**",
            "/api-docs/swagger-config",
            "/aggregate/**" // ✅ Allow API docs under aggregate
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // 🔹 Disable CSRF for API calls
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_URLS).permitAll() // ✅ Allow all API Docs URLs
                        .anyRequest().authenticated()) // 🔒 Secure all other endpoints
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()))
                .build();
    }
}
