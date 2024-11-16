package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.model.SaleItem;
import com._sale._Sale_Backend.model.dto.CategoryRevenueDTO;
import com._sale._Sale_Backend.repo.CategoryRepo;
import com._sale._Sale_Backend.repo.ProductRepo;
import com._sale._Sale_Backend.repo.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ProductStatsService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private CategoryRepo categoryRepo;
    
    @Autowired
    private SaleRepo saleRepo;

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

    public Map<String, Object> getCategoryRevenue() {
        List<Sale> sales = saleRepo.findAll(); // Use the injected SaleRepo here
        Map<String, CategoryRevenueDTO> categoryRevenueMap = new HashMap<>();

        // Populate categoryRevenueMap
        for (Sale sale : sales) {
            for (SaleItem saleItem : sale.getSales()) {
                String category = saleItem.getProduct().getCategory();
                BigDecimal revenue = saleItem.getTotalPrice();
                int quantitySold = saleItem.getQuantity();

                CategoryRevenueDTO categoryRevenueDTO = categoryRevenueMap.getOrDefault(category, new CategoryRevenueDTO(category, BigDecimal.ZERO, 0));

                // Update revenue and total sales for this category
                categoryRevenueDTO.setTotalRevenue(categoryRevenueDTO.getTotalRevenue().add(revenue));
                categoryRevenueDTO.setTotalSales(categoryRevenueDTO.getTotalSales() + quantitySold);

                categoryRevenueMap.put(category, categoryRevenueDTO);
            }
        }

        // Transform categoryRevenueMap into the required JSON structure
        Map<String, Object> response = new HashMap<>();
        List<String> categories = new ArrayList<>();
        List<BigDecimal> totalRevenue = new ArrayList<>();
        List<Integer> totalSales = new ArrayList<>();

        for (CategoryRevenueDTO dto : categoryRevenueMap.values()) {
            categories.add(dto.getCategory());
            totalRevenue.add(dto.getTotalRevenue());
            totalSales.add(dto.getTotalSales());
        }

        response.put("categories", categories);
        response.put("totalRevenue", totalRevenue);
        response.put("totalSales", totalSales);

        return response;
    }

    public Long getOutOfStockCount() {
        return productRepo.countOutOfStockProducts();
    }
}
