package com.example.web_store.controller;

import com.example.web_store.dto.CartItemRequest;
import com.example.web_store.dto.DtoRequest;
import com.example.web_store.dto.DtoResponseCart;
import com.example.web_store.dto.DtoResponseFoods;
import com.example.web_store.service.WebStoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class WebStoreController {

    private final WebStoreService webStoreService;

    @GetMapping("/foods")
    public ResponseEntity<List<DtoResponseFoods>> getAllFoods() {
        log.info("Fetching all foods");
        return ResponseEntity.ok(webStoreService.showAllFoods());
    }

    @PostMapping("/foods")
    public ResponseEntity<DtoResponseFoods> addFood(@RequestBody DtoRequest dtoRequest) {
        log.info("Adding new food: {}", dtoRequest);
        DtoResponseFoods response = webStoreService.addFood(dtoRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/cart")
    public ResponseEntity<DtoResponseCart> createCart() {
        log.info("Creating new cart");
        DtoResponseCart cart = webStoreService.createCart();
        return ResponseEntity.status(HttpStatus.CREATED).body(cart);
    }

    @PostMapping("/cart/{cartId}/items")
    public ResponseEntity<DtoResponseCart> addItemsToCart(
            @PathVariable Long cartId,
            @RequestBody List<CartItemRequest> items) {
        log.info("Adding items to cart {}: {}", cartId, items);
        DtoResponseCart cart = webStoreService.addItemsToCart(cartId, items);
        return ResponseEntity.ok(cart);
    }

    @GetMapping("/cart/{cartId}")
    public ResponseEntity<DtoResponseCart> getCart(@PathVariable Long cartId) {
        log.info("Fetching cart with id: {}", cartId);
        return ResponseEntity.ok(webStoreService.getCart(cartId));
    }

    @PutMapping("/cart/{cartId}/items/{itemId}")
    public ResponseEntity<DtoResponseCart> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        log.info("Updating item {} in cart {} to quantity {}", itemId, cartId, quantity);
        DtoResponseCart cart = webStoreService.updateCartItemQuantity(cartId, itemId, quantity);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart/{cartId}/items/{itemId}")
    public ResponseEntity<DtoResponseCart> removeItemFromCart(
            @PathVariable Long cartId,
            @PathVariable Long itemId) {
        log.info("Removing item {} from cart {}", itemId, cartId);
        DtoResponseCart cart = webStoreService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/cart/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        log.info("Deleting cart: {}", cartId);
        webStoreService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/cart/{cartId}/clear")
    public ResponseEntity<DtoResponseCart> clearCart(@PathVariable Long cartId) {
        log.info("Clearing cart: {}", cartId);
        DtoResponseCart cart = webStoreService.clearCart(cartId);
        return ResponseEntity.ok(cart);
    }
}