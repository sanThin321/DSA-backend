package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.service.SaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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
}
