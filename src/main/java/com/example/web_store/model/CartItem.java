package com.example.web_store.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_item")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", nullable = false)
    private Foods food;

    @Column(name = "quantity", nullable = false)
    private Integer quantity = 1;

    public BigDecimal getTotalPrice() {
        return BigDecimal.valueOf(food.getPrice())
                .multiply(BigDecimal.valueOf(quantity));
    }
}