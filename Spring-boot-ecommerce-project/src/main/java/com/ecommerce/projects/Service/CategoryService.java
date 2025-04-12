package com.ecommerce.projects.Service;

import com.ecommerce.projects.Payload.CategoryDTO;
import com.ecommerce.projects.Payload.CategoryResponse;
import com.ecommerce.projects.model.Category;

public interface CategoryService {
    CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);
    CategoryDTO createCategory(CategoryDTO categoryDTO);
    CategoryDTO deleteCategory(Long categoryId);
    CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId);
}