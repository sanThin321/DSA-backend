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
}
