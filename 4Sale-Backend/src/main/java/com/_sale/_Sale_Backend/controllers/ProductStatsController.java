package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.model.dto.CategoryRevenueDTO;
import com._sale._Sale_Backend.service.ProductStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/stats/product/")
@CrossOrigin("http://localhost:5173")
public class ProductStatsController {

    @Autowired
    private ProductStatsService productService;

    // API to get the total number of products
    @GetMapping("/count")
    public ResponseEntity<Long> getTotalNumberOfProducts() {
        long totalProducts = productService.getTotalNumberOfProducts();
        return ResponseEntity.ok(totalProducts);
    }

    // API to get products with low stock (quantity < thresholdValue)
    @GetMapping("/low-stock")
    public ResponseEntity<List<Product>> getProductsWithLowStock() {
        List<Product> lowStockProducts = productService.getProductsWithLowStock();
        return ResponseEntity.ok(lowStockProducts);
    }

    @GetMapping("/category/count")
    public ResponseEntity<Long> getTotalNumberOfCategory() {
        long totalCategory = productService.getTotalNumberOfCategory();
        return ResponseEntity.ok(totalCategory);
    }

    @GetMapping("/category-revenue")
    public Map<String, Object> getCategoryRevenue() {
        return productService.getCategoryRevenue();
    }

    @GetMapping("/out-of-stock-count")
    public ResponseEntity<Long> getOutOfStockCount() {
        Long outOfStockCount = productService.getOutOfStockCount();
        return ResponseEntity.ok(outOfStockCount);
    }
}
