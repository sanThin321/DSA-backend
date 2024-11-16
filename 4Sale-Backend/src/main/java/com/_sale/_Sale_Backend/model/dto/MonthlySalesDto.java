package com._sale._Sale_Backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MonthlySalesDto {
    private String month;
    private double totalRevenue;
    private int totalProductSold;
}
