package com._sale._Sale_Backend.mode.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class RevenueByDateDTO {
    private String saleDate;
    private BigDecimal totalRevenue;
}
