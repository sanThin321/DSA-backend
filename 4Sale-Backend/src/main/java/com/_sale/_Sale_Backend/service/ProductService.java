package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.repo.ProductRepo;
import com._sale._Sale_Backend.utils.CustomHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;
    private final CustomHashMap<Integer, Product> productCache = new CustomHashMap<>();


    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    // Get product by ID (check cache first, then DB)
    public Product getProductById(int id) {
        // Check if the product is already cached
        Product cachedProduct = productCache.get(id);
        if (cachedProduct != null) {
            return cachedProduct;
        }

        // If not cached, retrieve from database and store in cache
        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            productCache.put(id, product);  // Cache the product
            return product;
        } else {
            return new Product(-1);
        }
    }

    // Add or update product (store in DB and cache)
    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        product.setImageName(image.getOriginalFilename());
        product.setImageType(image.getContentType());
        product.setImageData(image.getBytes());

        // Save to the database
        Product savedProduct = productRepo.save(product);

        // Store in cache
        productCache.put(savedProduct.getProductId(), savedProduct);
        return savedProduct;
    }

    // Delete product (remove from both DB and cache)
    public void deleteById(int id) {
        // Remove from database
        productRepo.deleteById(id);

        // Remove from cache
        productCache.remove(id);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }
}
