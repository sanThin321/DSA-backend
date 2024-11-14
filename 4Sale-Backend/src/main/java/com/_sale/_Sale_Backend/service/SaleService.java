package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.model.SaleItem;
import com._sale._Sale_Backend.repo.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
}
