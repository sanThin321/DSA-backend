// In CategoryService.java

package com._sale._Sale_Backend.service;

import com._sale._Sale_Backend.model.Category;
import com._sale._Sale_Backend.repo.CategoryRepo;
import com._sale._Sale_Backend.utils.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepo categoryRepo;

    private LinkedList<Category> categoryQueue = new LinkedList<>();  // Generic Linked List

    // Add a category using the linked list before persisting
    public Category addCategory(Category category) {
        categoryQueue.add(category);  // Add to the linked list

        // Print the linked list (for debugging)
        categoryQueue.printList();

        // Remove from the list and persist the first category to the database
        return categoryRepo.save(categoryQueue.remove());  // Persist from linked list
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
