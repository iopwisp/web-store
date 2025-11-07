package com.example.web_store.exception;

public class CartItemNotFoundException extends WebStoreException {
    public CartItemNotFoundException(String message) {
        super(message);
    }
}
