package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getProduct() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    // get product by id
    @GetMapping("/product/{id}")
    public ResponseEntity<Product> getProductBtId(@PathVariable int id) {
        Product product = productService.getProductById(id);

        if (product.getProductId() > -1) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }

    // to add products
    @PostMapping(value = "/product", consumes = {"multipart/form-data"})
    public ResponseEntity<?> addProduct(@RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product savedProduct = null;
        try {
            savedProduct = productService.addOrUpdateProduct(product, imageFile);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // to update product by id
    @PutMapping("/product/{id}")
    public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestPart Product product, @RequestPart MultipartFile imageFile) {
        Product updatedProduct = null;
        try {
            updatedProduct = productService.addOrUpdateProduct(product, imageFile);
            return new ResponseEntity<>("Updated", HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    // delete product
    @DeleteMapping("product/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable int id) {
        Product product = productService.getProductById(id);

        if (product != null) {
            productService.deleteById(id);
            return new ResponseEntity<>("Deleted.",HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

//    // search products
//    @GetMapping("/products/search")
//    public ResponseEntity<List<Product>> searchProducts(@RequestParam String keyword) {
//        List<Product> products = productService.searchProducts(keyword);
//        return new ResponseEntity<>(products, HttpStatus.OK);
//    }

    // Search products by character with optional sorting
    @GetMapping("/product/search")
    public List<Product> searchProducts(
            @RequestParam("query") String query,
            @RequestParam(value = "sortBy", defaultValue = "name") String sortBy
    ) {
        return productService.searchProducts(query, sortBy);
    }
}
