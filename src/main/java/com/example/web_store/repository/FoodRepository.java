package com.example.web_store.repository;

import com.example.web_store.model.Foods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Foods , Long> {
}
