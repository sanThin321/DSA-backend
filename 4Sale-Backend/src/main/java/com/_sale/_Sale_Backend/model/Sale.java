package com._sale._Sale_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    private String customerName;
    private String contactNumber;
    private String paymentMethod;
    private String journalNumber;
    private BigDecimal totalAmount;
    private String saleDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "sale_id")
    private List<SaleItem> sales;

    public int compareTo(Sale other) {
        if (this.customerName == null || other.customerName == null) {
            return 0; // Handle null customerName gracefully
        }
        return this.customerName.compareToIgnoreCase(other.customerName);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Sale sale = (Sale) obj;
        if (this.customerName == null || sale.customerName == null) return false;

        // Compare customerName (case-insensitive)
        return this.customerName.equalsIgnoreCase(sale.customerName);
    }
}

