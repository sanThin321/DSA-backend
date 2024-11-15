package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.mode.dto.RevenueByDateDTO;
import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.model.dto.ProductSalesDTO;
import com._sale._Sale_Backend.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/sale")
@CrossOrigin("http://localhost:5173")
public class SaleController {

    @Autowired
    private SaleService saleService;

    @PostMapping("/add")
    public ResponseEntity<Sale> createSale(@RequestBody Sale sale) throws IOException {
        Sale savedSale = saleService.saveSale(sale);
//        return ResponseEntity.ok(savedSale);
        return new ResponseEntity<>(savedSale, HttpStatus.CREATED);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAllSales() {
        List<Sale> sales = saleService.getAllSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Sale> getSaleById(@PathVariable Long id) {
        Sale sale = saleService.getSaleById(id);
        if (sale != null) {
            return ResponseEntity.ok(sale);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteSale(@PathVariable Long id) {
        saleService.deleteSale(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/top-selling-products-by-date/{saleDate}")
    public ResponseEntity<?> getTopSellingProductsByDate(@PathVariable String saleDate) {
        try {
            // Fetch the top-selling products for the given date
            List<ProductSalesDTO> topSellingProducts = saleService.getTopSellingProductsByDate(saleDate);

            // Calculate total revenue for these products outside of the response definition
            double calculatedTotalRevenue = topSellingProducts.stream()
                    .mapToDouble(ProductSalesDTO::getTotalRevenue)
                    .sum();

            // Prepare and return the response
            return ResponseEntity.ok(new Object() {
                public final List<ProductSalesDTO> products = topSellingProducts;
                public final double totalRevenue = calculatedTotalRevenue; // Use calculated value here
            });
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/revenue-by-date")
    public ResponseEntity<BigDecimal> getRevenueByDate(@RequestParam String saleDate) {
        BigDecimal totalRevenue = saleService.getTotalRevenueBySaleDate(saleDate);
        return ResponseEntity.ok(totalRevenue);
    }

    @GetMapping("/sales-count-by-date")
    public ResponseEntity<Long> getTotalSalesByDate(@RequestParam String saleDate) {
        Long totalSalesCount = saleService.getTotalSalesByDate(saleDate);
        return ResponseEntity.ok(totalSalesCount);
    }
}
