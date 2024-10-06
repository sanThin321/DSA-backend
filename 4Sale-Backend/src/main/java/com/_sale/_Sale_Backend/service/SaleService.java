package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Sale;
import com._sale._Sale_Backend.repo.SaleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SaleService {

    @Autowired
    private SaleRepo saleRepo;

    public Sale saveSale(Sale sale) {
        return saleRepo.save(sale);
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
