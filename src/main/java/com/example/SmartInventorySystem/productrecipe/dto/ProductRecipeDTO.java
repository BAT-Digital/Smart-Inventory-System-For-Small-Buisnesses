package com.example.SmartInventorySystem.productrecipe.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRecipeDTO {
    Long finalProductId;
    Long ingredientId;
    BigDecimal quantityRequired;
}
