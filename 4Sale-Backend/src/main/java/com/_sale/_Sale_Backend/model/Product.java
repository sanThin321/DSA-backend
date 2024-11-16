package com._sale._Sale_Backend.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productId;
    private String name;
    private int quantity;
    private BigDecimal price;
    private String category;
    private int thresholdValue;
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expirationDate;
    private boolean productAvailable;
    private String imageName;
    private String imageType;
    private String imageData;

    public Product(int id) {
        this.productId = id;
    }
}
