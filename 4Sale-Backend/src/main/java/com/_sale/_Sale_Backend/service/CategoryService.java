package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Category;
import com._sale._Sale_Backend.model.Product;
import com._sale._Sale_Backend.repo.CategoryRepo;
import com._sale._Sale_Backend.utils.CustomHashMap;
import com._sale._Sale_Backend.utils.MergeSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

    // Get a category by name, with cache lookup if necessary
    public Category getCategoryByName(String name) {
        // First, check cache for the category
        for (Category cachedCategory : categoryCache.values()) {
            if (cachedCategory.getName().equalsIgnoreCase(name)) {
                return cachedCategory;
            }
        }
        // If not found in cache, query the database
        Category categoryFromDb = categoryRepo.findByName(name);
        if (categoryFromDb != null) {
            categoryCache.put(categoryFromDb.getCategoryId(), categoryFromDb); // Cache it
        }
        return categoryFromDb;
    }

    // Get a category by ID, leveraging cache for performance
    public Category getCategoryById(int id) {
        // Check cache first
        Category cachedCategory = categoryCache.get(id);
        if (cachedCategory != null) {
            return cachedCategory;
        }
        // Fetch from DB if not in cache
        Category categoryFromDb = categoryRepo.findById(id).orElse(null);
        if (categoryFromDb != null) {
            categoryCache.put(id, categoryFromDb); // Cache it
        }
        return categoryFromDb;
    }

    // Get all categories: load from cache or database
    public List<Category> getAllCategories() {
        // If cache is empty, load from DB and populate cache
        if (categoryCache.isEmpty()) {
            List<Category> categories = categoryRepo.findAll();
            for (Category category : categories) {
                categoryCache.put(category.getCategoryId(), category);
            }
        }

        // Sort the categories by name alphabetically using MergeSort
        return MergeSort.mergeSort(new ArrayList<>(categoryCache.values()), Comparator.comparing(Category::getName));
    }


    // Delete a category by ID: remove from both cache and database
    public boolean deleteCategory(int id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);  // Remove from DB
            categoryCache.remove(id);    // Remove from cache
            return true;
        }
        return false;
    }
}
