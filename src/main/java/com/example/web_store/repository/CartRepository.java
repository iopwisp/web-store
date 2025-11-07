package com.example.web_store.repository;

import com.example.web_store.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart , Long>{}