package com.techy.microservices.apigateway_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
            "/aggregate/**" // Allow API docs under aggregate
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable()) // Disable CSRF for API calls
                .cors(Customizer.withDefaults()) // Enable CORS (uses corsFilter bean below)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(PUBLIC_URLS).permitAll() // Allow public URLs
                        .anyRequest().authenticated()) // Secure all other endpoints
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults())) // OAuth2 with JWT
                .build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true); // Allow cookies/credentials (needed for OAuth2)
        config.addAllowedOrigin("http://localhost:4200"); // Allow Angular frontend
        config.addAllowedHeader("*"); // Allow all headers (e.g., Authorization, Content-Type)
        config.addAllowedMethod("*"); // Allow all methods (GET, POST, OPTIONS, etc.)
        source.registerCorsConfiguration("/api/**", config); // Apply to /api/** endpoints
        return new CorsFilter(source);
    }
}