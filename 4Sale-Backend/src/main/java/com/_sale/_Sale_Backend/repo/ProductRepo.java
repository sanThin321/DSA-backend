package com._sale._Sale_Backend.repo;


import com._sale._Sale_Backend.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Integer> {

    @Query("SELECT p from Product p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(p.category) LIKE LOWER(CONCAT('%', :keyword, '%'))"
    )
    List<Product> searchProducts(String keyword);

    @Query("SELECT p FROM Product p WHERE p.quantity < p.thresholdValue")
    List<Product> findByQuantityLessThanThreshold();

    @Query("SELECT COUNT(p) FROM Product p WHERE p.quantity <= 0")
    Long countOutOfStockProducts();
}
