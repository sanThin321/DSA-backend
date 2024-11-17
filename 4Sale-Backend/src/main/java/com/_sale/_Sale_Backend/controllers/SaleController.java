package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.model.dto.MonthlySalesDto;
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
    public ResponseEntity<String> deleteSale(@PathVariable Long id) {
        boolean isDeleted = saleService.deleteSale(id);

        if (isDeleted) {
            return ResponseEntity.ok("Sale successfully deleted."); // 200 OK with a success message
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sale not found."); // 404 Not Found with a message
        }
    }



    @GetMapping("/top-selling-products-by-date/{saleDate}")
    public ResponseEntity<?> getTopSellingProductsByDate(@PathVariable String saleDate) {
        try {
            // Fetch the top-selling products for the given date
            List<ProductSalesDTO> topSellingProducts = saleService.getTopSellingProductsByDate(saleDate);

            // Calculate total revenue for these products outside the response definition
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

    @GetMapping("/sale-stats/monthly")
    public List<MonthlySalesDto> getMonthlyProductSalesAndRevenue() {
        return saleService.getMonthlyProductSalesAndRevenue();
    }

    @GetMapping("/search")
    public List<Sale> searchSales(@RequestParam String query) {
        return saleService.searchSales(query);
    }

}
