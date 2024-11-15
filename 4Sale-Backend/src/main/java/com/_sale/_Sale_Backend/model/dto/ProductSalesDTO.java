package com._sale._Sale_Backend.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ProductSalesDTO {

    // Getters and Setters
    private String productName;
    private BigDecimal productPrice;
    private Long totalQuantitySold;
    private Double totalRevenue;
    private String imageUrl;
    private int remainingQuantity;

    public ProductSalesDTO(String productName, BigDecimal productPrice, Long totalQuantitySold, Double totalRevenue, String productImageUrl, int quantity) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.totalQuantitySold = totalQuantitySold;
        this.totalRevenue = totalRevenue;
        this.imageUrl = productImageUrl;
        this.remainingQuantity = quantity;
    }

}
