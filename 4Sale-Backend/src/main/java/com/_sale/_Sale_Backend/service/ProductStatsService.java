package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.repo.CategoryRepo;
import com._sale._Sale_Backend.repo.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductStatsService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    // Method to get total number of products
    public long getTotalNumberOfProducts() {
        return productRepo.count();
    }

    // Method to get products with low stock (quantity < thresholdValue)
    public List<Product> getProductsWithLowStock() {
        return productRepo.findByQuantityLessThanThreshold();
    }

    // Method to get total numbers of category
    public long getTotalNumberOfCategory() {
        return categoryRepo.count();
    }
}
