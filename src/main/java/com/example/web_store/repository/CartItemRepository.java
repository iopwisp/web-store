package com.example.web_store.repository;

import com.example.web_store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem , Long> {
}
