package com._sale._Sale_Backend.service;

//import com._sale._Sale_Backend.config.CloudinaryConfig;
import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.repo.ProductRepo;
import com._sale._Sale_Backend.utils.CustomHashMap;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private Cloudinary cloudinary;

    private final CustomHashMap<Integer, Product> productCache = new CustomHashMap<>();

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProductById(int id) {
        Product cachedProduct = productCache.get(id);
        if (cachedProduct != null) {
            return cachedProduct;
        }

        Optional<Product> productOpt = productRepo.findById(id);
        if (productOpt.isPresent()) {
            Product product = productOpt.get();
            productCache.put(id, product);
            return product;
        } else {
            return new Product(-1);
        }
    }

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

        Product savedProduct = productRepo.save(product);
        productCache.put(savedProduct.getProductId(), savedProduct);
        return savedProduct;
    }

    public void deleteById(int id) {
        productRepo.deleteById(id);
        productCache.remove(id);
    }

    public List<Product> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword);
    }

    public void updateProductQuantity(Product product) {
        productRepo.save(product);
    }

    // search products
    public List<Product> searchProducts(String query, String sortBy) {
        // Fetch all products
        List<Product> allProducts = productRepo.findAll();

        // Filter products by query (case insensitive)
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : allProducts) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredProducts.add(product);
            }
        }

        // Sort filtered products using merge sort
        return mergeSort(filteredProducts, sortBy.toLowerCase());
    }

    private List<Product> mergeSort(List<Product> products, String sortBy) {
        if (products.size() <= 1) {
            return products;
        }

        // Divide the list into two halves
        int mid = products.size() / 2;
        List<Product> left = mergeSort(products.subList(0, mid), sortBy);
        List<Product> right = mergeSort(products.subList(mid, products.size()), sortBy);

        // Merge the sorted halves
        return merge(left, right, sortBy);
    }

    private List<Product> merge(List<Product> left, List<Product> right, String sortBy) {
        List<Product> merged = new ArrayList<>();
        int i = 0, j = 0;

        // Merge two lists based on the sorting criteria
        while (i < left.size() && j < right.size()) {
            if (compareProducts(left.get(i), right.get(j), sortBy) <= 0) {
                merged.add(left.get(i));
                i++;
            } else {
                merged.add(right.get(j));
                j++;
            }
        }

        // Add remaining elements from left
        while (i < left.size()) {
            merged.add(left.get(i));
            i++;
        }

        // Add remaining elements from right
        while (j < right.size()) {
            merged.add(right.get(j));
            j++;
        }

        return merged;
    }

    private int compareProducts(Product p1, Product p2, String sortBy) {
        switch (sortBy) {
            case "price":
                return p1.getPrice().compareTo(p2.getPrice());
            case "quantity":
                return Integer.compare(p1.getQuantity(), p2.getQuantity());
            case "expirationdate":
                return p1.getExpirationDate().compareTo(p2.getExpirationDate());
            default: // Default sorting by name
                return p1.getName().compareToIgnoreCase(p2.getName());
        }
    }
}
