package com._sale._Sale_Backend.repo;

import com._sale._Sale_Backend.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepo extends JpaRepository<Sale, Long> {
}
