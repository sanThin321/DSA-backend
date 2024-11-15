package com._sale._Sale_Backend.repo;

import com._sale._Sale_Backend.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SaleRepo extends JpaRepository<Sale, Long> {
    // Custom query to get top 5 best-selling products by total sales quantity and total revenue
    @Query("SELECT si.product, SUM(si.quantity) AS totalQuantity, SUM(si.totalPrice) AS totalRevenue " +
            "FROM Sale s " +
            "JOIN s.sales si " +
            "WHERE s.saleDate = :saleDate " +
            "GROUP BY si.product " +
            "ORDER BY totalQuantity DESC")
    List<Object[]> getTopSellingProductsByDate(String saleDate);
}
