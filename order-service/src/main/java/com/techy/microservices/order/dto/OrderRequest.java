package com.techy.microservices.order.dto;


import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

import java.math.BigDecimal;

public record OrderRequest(
        String orderNumber,
        String skuCode,
        BigDecimal price,
        Integer quantity,

        UserDetails  userDetails
) {
}