package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Category;
import com._sale._Sale_Backend.repo.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    // Add a category
    public Category addCategory(Category category) {
        return categoryRepo.save(category);
    }

    // Get all categories
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    // Delete a category by ID
    public boolean deleteCategory(int id) {
        if (categoryRepo.existsById(id)) {
            categoryRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
