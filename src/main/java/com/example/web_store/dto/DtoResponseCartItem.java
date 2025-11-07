package com.example.web_store.dto;

import java.math.BigDecimal;

public record DtoResponseCartItem(
        Long id,
        DtoResponseFoods food,
        Integer quantity,
        BigDecimal totalPrice
) {
}
