package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.mode.dto.RevenueByDateDTO;
import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.model.SaleItem;
import com._sale._Sale_Backend.model.dto.MonthlySalesDto;
import com._sale._Sale_Backend.model.dto.ProductSalesDTO;
import com._sale._Sale_Backend.repo.SaleRepo;
import com._sale._Sale_Backend.utils.SaleSearchUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SaleService {

    @Autowired
    private SaleRepo saleRepo;

    @Autowired
    private ProductService productService;

    @Transactional
    public Sale saveSale(Sale sale) {
        // Save the sale first
        Sale savedSale = saleRepo.save(sale);

        // Loop through each SaleItem and update product quantity
        for (SaleItem saleItem : sale.getSales()) {
            Product product = productService.getProductById(saleItem.getProduct().getProductId());

            // Subtract the quantity sold from the product's quantity
            if (product.getQuantity() >= saleItem.getQuantity()) {
                product.setQuantity(product.getQuantity() - saleItem.getQuantity());
                productService.updateProductQuantity(product);
            } else {
                throw new IllegalArgumentException("Insufficient quantity for product: " + product.getName());
            }
        }

        return savedSale;
    }

    public List<Sale> getAllSales() {
        return saleRepo.findAll();
    }

    public Sale getSaleById(Long id) {
        return saleRepo.findById(id).orElse(null);
    }

    public void deleteSale(Long id) {
        saleRepo.deleteById(id);
    }

    public List<ProductSalesDTO> getTopSellingProductsByDate(String saleDate) {
        List<Object[]> results = saleRepo.getTopSellingProductsByDate(saleDate);

        return results.stream()
                .limit(5)  // Get top 5 products
                .map(result -> {
                    Product product = (Product) result[0];
                    Long totalQuantity = (Long) result[1];
                    BigDecimal totalRevenue = (BigDecimal) result[2];

                    return new ProductSalesDTO(
                            product.getName(),
                            product.getPrice(),
                            totalQuantity,
                            totalRevenue.doubleValue(),
                            product.getImageData(),
                            product.getQuantity()
                    );
                })
                .collect(Collectors.toList());
    }


    public BigDecimal getTotalRevenueBySaleDate(String saleDate) {
        return saleRepo.getTotalRevenueBySaleDate(saleDate);
    }

    public Long getTotalSalesByDate(String saleDate) {
        return saleRepo.getTotalSalesByDate(saleDate);
    }

    public List<MonthlySalesDto> getMonthlyProductSalesAndRevenue() {
        List<Object[]> results = saleRepo.getMonthlyProductSalesAndRevenue();
        return results.stream()
                .map(result -> new MonthlySalesDto(
                        Month.of(((Number) result[0]).intValue()).toString(), // Convert month number to name
                        ((Number) result[2]).doubleValue(), // Total revenue
                        ((Number) result[1]).intValue()     // Total products sold
                ))
                .collect(Collectors.toList());
    }

    public List<Sale> searchSales(String query) {
        List<Sale> allSales = saleRepo.findAll();

        // Sort the sales using Bubble Sort (or you could use a better sorting algorithm like Merge Sort)
        SaleSearchUtil.bubbleSortByName(allSales);

        // Perform linear search for exact or partial matches by customerName
        // Now that it's sorted, we can search through the sorted list
        return SaleSearchUtil.linearSearchByName(allSales, query);
    }



}
