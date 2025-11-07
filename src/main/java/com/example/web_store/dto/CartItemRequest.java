package com.example.web_store.dto;

public record CartItemRequest(
        Long foodId,
        Integer quantity
) {
}
