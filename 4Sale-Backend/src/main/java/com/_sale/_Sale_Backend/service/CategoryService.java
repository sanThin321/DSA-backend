package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Category;
import com._sale._Sale_Backend.repo.CategoryRepo;
import com._sale._Sale_Backend.utils.CustomHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    private final CustomHashMap<Integer, Category> categoryCache = new CustomHashMap<>();

    // Add a category: save to cache and persist to DB
    public Category addCategory(Category category) {
        categoryCache.put(category.getCategoryId(), category);  // Cache the category
        return categoryRepo.save(category);  // Persist to DB
    }

    // Get a category by ID from cache or DB
    public Category getCategoryById(int id) {
        Category cachedCategory = categoryCache.get(id);
        if (cachedCategory != null) {
            return cachedCategory;
        }

        return categoryRepo.findById(id).orElse(null);
    }

    // Get all categories from the database
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // Delete a category by ID
    public boolean deleteCategory(int id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);  // Remove from DB
            categoryCache.remove(id);     // Remove from cache
            return true;
        }
        return false;
    }
}
