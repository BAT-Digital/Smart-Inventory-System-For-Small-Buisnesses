package com.example.SmartInventorySystem.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
@Entity
@Table(name = "product_recipe")
public class ProductRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long recipeId;

    @ManyToOne
    @JoinColumn(name = "final_product_id", referencedColumnName = "product_id", nullable = false)
    private Product finalProduct;

    @ManyToOne
    @JoinColumn(name = "ingredient_id", referencedColumnName = "product_id", nullable = false)
    private Product ingredient;

    @Column(name = "quantity_required", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantityRequired;
}