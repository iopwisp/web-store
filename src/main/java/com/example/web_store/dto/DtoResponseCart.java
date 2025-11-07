package com.example.web_store.dto;

import java.math.BigDecimal;

public record DtoResponseCart(
        Long id,
        java.util.List<DtoResponseCartItem> items,
        BigDecimal totalPrice
) {
}
