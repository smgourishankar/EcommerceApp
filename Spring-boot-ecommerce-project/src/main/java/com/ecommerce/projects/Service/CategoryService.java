package com.ecommerce.projects.Service;

import com.ecommerce.projects.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    void createCategory(Category category);
    String deleteCategory(Long categoryId);
    Category updateCategory(Category category, Long categoryId);
}