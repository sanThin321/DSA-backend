package com._sale._Sale_Backend.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CategoryRevenueDTO {
    private String category;
    private BigDecimal totalRevenue;
    private int totalSales;

    // Constructors, getters, and setters
    public CategoryRevenueDTO(String category, BigDecimal totalRevenue, int totalSales) {
        this.category = category;
        this.totalRevenue = totalRevenue;
        this.totalSales = totalSales;
    }
}
