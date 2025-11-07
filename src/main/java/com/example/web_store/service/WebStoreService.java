package com.example.web_store.service;

import com.example.web_store.dto.*;
import com.example.web_store.exception.CartNotFoundException;
import com.example.web_store.exception.FoodNotFoundException;
import com.example.web_store.exception.CartItemNotFoundException;
import com.example.web_store.model.Cart;
import com.example.web_store.model.CartItem;
import com.example.web_store.model.Foods;
import com.example.web_store.repository.CartRepository;
import com.example.web_store.repository.FoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WebStoreService {

    private final FoodRepository foodRepository;
    private final CartRepository cartRepository;

    @Transactional(readOnly = true)
    public List<DtoResponseFoods> showAllFoods() {
        return foodRepository.findAll().stream()
                .map(this::mapToFoodDto)
                .toList();
    }

    public DtoResponseFoods addFood(DtoRequest dtoRequest) {
        Foods food = new Foods();
        food.setName(dtoRequest.name());
        food.setPrice(dtoRequest.price());
        if (dtoRequest.description() != null) {
            food.setDescription(dtoRequest.description());
        }

        Foods savedFood = foodRepository.save(food);
        return mapToFoodDto(savedFood);
    }

    public DtoResponseCart createCart() {
        Cart cart = new Cart();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartDto(savedCart);
    }

    @Transactional(readOnly = true)
    public DtoResponseCart getCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));
        return mapToCartDto(cart);
    }

    public DtoResponseCart addItemsToCart(Long cartId, List<CartItemRequest> itemRequests) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        for (CartItemRequest request : itemRequests) {
            Foods food = foodRepository.findById(request.foodId())
                    .orElseThrow(() -> new FoodNotFoundException("Food not found with id: " + request.foodId()));


            CartItem existingItem = cart.getItems().stream()
                    .filter(item -> item.getFood().getId().equals(request.foodId()))
                    .findFirst()
                    .orElse(null);

            if (existingItem != null) {

                existingItem.setQuantity(existingItem.getQuantity() + request.quantity());
            } else {
                CartItem cartItem = new CartItem();
                cartItem.setFood(food);
                cartItem.setQuantity(request.quantity());
                cart.addItem(cartItem);
            }
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToCartDto(savedCart);
    }

    public DtoResponseCart updateCartItemQuantity(Long cartId, Long itemId, Integer quantity) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + itemId));

        if (quantity <= 0) {
            cart.removeItem(cartItem);
        } else {
            cartItem.setQuantity(quantity);
        }

        Cart savedCart = cartRepository.save(cart);
        return mapToCartDto(savedCart);
    }

    public DtoResponseCart removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new CartItemNotFoundException("Cart item not found with id: " + itemId));

        cart.removeItem(cartItem);
        Cart savedCart = cartRepository.save(cart);
        return mapToCartDto(savedCart);
    }

    public DtoResponseCart clearCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));

        cart.getItems().clear();
        Cart savedCart = cartRepository.save(cart);
        return mapToCartDto(savedCart);
    }

    public void deleteCart(Long cartId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id: " + cartId));
        cartRepository.delete(cart);
    }


    private DtoResponseFoods mapToFoodDto(Foods food) {
        return new DtoResponseFoods(
                food.getName(),
                food.getPrice(),
                food.getId()
                );
    }

    private DtoResponseCart mapToCartDto(Cart cart) {
        List<DtoResponseCartItem> items = cart.getItems().stream()
                .map(this::mapToCartItemDto)
                .collect(Collectors.toList());

        BigDecimal totalPrice = cart.getItems().stream()
                .map(CartItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DtoResponseCart(
                cart.getId(),
                items,
                totalPrice
        );
    }

    private DtoResponseCartItem mapToCartItemDto(CartItem item) {
        return new DtoResponseCartItem(
                item.getId(),
                mapToFoodDto(item.getFood()),
                item.getQuantity(),
                item.getTotalPrice()
        );
    }
}