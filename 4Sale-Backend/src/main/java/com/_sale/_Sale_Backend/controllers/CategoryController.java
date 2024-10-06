package com._sale._Sale_Backend.controllers;

import com._sale._Sale_Backend.model.Category;
import com._sale._Sale_Backend.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    // Add a category
    @PostMapping("/addCategory")
    public ResponseEntity<?> addCategory(@RequestBody Category category) {
        Category savedCategory = categoryService.addCategory(category);
        return new ResponseEntity<>(savedCategory, HttpStatus.CREATED);
    }

    // Get all categories
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Delete a category by ID
    @DeleteMapping("/category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        boolean isRemoved = categoryService.deleteCategory(id);
        if (isRemoved) {
            return new ResponseEntity<>("Category deleted successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Category not found", HttpStatus.NOT_FOUND);
        }
    }
}
