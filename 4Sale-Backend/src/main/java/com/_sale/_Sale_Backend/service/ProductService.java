package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.repo.ProductRepo;
import com._sale._Sale_Backend.utils.CustomHashMap;
import com._sale._Sale_Backend.utils.LinearSearch;
import com._sale._Sale_Backend.utils.MergeSort;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Cloudinary cloudinary;

    private final CustomHashMap<Integer, Product> productCache = new CustomHashMap<>();

    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();

        // Sort the products by name alphabetically using MergeSort
        return MergeSort.mergeSort(products, Comparator.comparing(Product::getName));
    }


    // Fetch product by ID from the cache, if not present, fetch from DB and add to cache
    public Product getProductById(int id) {
        Product cachedProduct = productCache.get(id);
        if (cachedProduct != null) {
            return cachedProduct;  // Return from cache if found
        }

        // Fetch from DB and add to cache if not found in cache
        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            productCache.put(id, product);  // Cache the product
            return product;
        } else {
            return new Product(-1);  // Return a default product if not found
        }
    }

    // Add or update product, including handling image upload to Cloudinary
    public Product addOrUpdateProduct(Product product, MultipartFile image) throws IOException {
        if (image != null && !image.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "folder", "4sale",
                    "quality", "auto",
                    "fetch_format", "auto",
                    "width", 400,
                    "height", 400,
                    "crop", "limit"
            ));
            String imageUrl = uploadResult.get("secure_url").toString();
            product.setImageName(image.getOriginalFilename());
            product.setImageType(image.getContentType());
            product.setImageData(imageUrl);
        }

        // Save the product in the database
        Product savedProduct = productRepo.save(product);

        // Cache the saved product
        productCache.put(savedProduct.getProductId(), savedProduct);

        return savedProduct;
    }

    // Delete product by ID, removing it from the cache as well
    public void deleteById(int id) {
        productRepo.deleteById(id);
        productCache.remove(id);  // Remove from cache
    }

    // Update product quantity, and sync with the cache
    public void updateProductQuantity(Product product) {
        productRepo.save(product);
        productCache.put(product.getProductId(), product);  // Update cache
    }

    // Search products by name, filter using LinearSearch, and sort results by name using MergeSort
    public List<Product> searchProducts(String query) {
        List<Product> allProducts = productRepo.findAll();

        // Filter products by query using LinearSearch
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : allProducts) {
            Product foundProduct = LinearSearch.linearSearch(allProducts, product);
            if (foundProduct != null && foundProduct.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(foundProduct);
            }
        }

        // Sort filtered products by name
        return MergeSort.mergeSort(filteredProducts, (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()));
    }

    // Edit existing product, updating its details, including image if provided
    public Product editProduct(int id, Product updatedProduct, MultipartFile image) throws IOException {
        Optional<Product> optionalProduct = productRepo.findById(id);

        if (optionalProduct.isEmpty()) {
            throw new IllegalArgumentException("Product with ID " + id + " not found");
        }

        Product existingProduct = optionalProduct.get();

        // Update product details
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setQuantity(updatedProduct.getQuantity());
        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setThresholdValue(updatedProduct.getThresholdValue());
        existingProduct.setExpirationDate(updatedProduct.getExpirationDate());
        existingProduct.setProductAvailable(updatedProduct.isProductAvailable());

        // Handle image upload if a new image is provided
        if (image != null && !image.isEmpty()) {
            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.asMap(
                    "folder", "4sale",
                    "quality", "auto",
                    "fetch_format", "auto",
                    "width", 400,
                    "height", 400,
                    "crop", "limit"
            ));
            String imageUrl = uploadResult.get("secure_url").toString();
            existingProduct.setImageName(image.getOriginalFilename());
            existingProduct.setImageType(image.getContentType());
            existingProduct.setImageData(imageUrl);
        }

        // Save the updated product to the database
        Product savedProduct = productRepo.save(existingProduct);

        // Update the cache
        productCache.put(savedProduct.getProductId(), savedProduct);

        return savedProduct;
    }
}
