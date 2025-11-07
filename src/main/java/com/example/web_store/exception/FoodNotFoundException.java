package com.example.web_store.exception;

public class FoodNotFoundException extends WebStoreException {
    public FoodNotFoundException(String message) {
        super(message);
    }
}
