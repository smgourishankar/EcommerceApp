package com.ecommerce.projects.Service;

import com.ecommerce.projects.exceptions.APIException;
import com.ecommerce.projects.exceptions.ResourceNotFoundException;
import com.ecommerce.projects.model.Category;
import com.ecommerce.projects.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

//    private List<Category> categories = new ArrayList<>();
//    private Long nextId= 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(!categories.isEmpty()){
            return categories;
        }
        else{
            throw new APIException("Categories not created till now..!!");
        }
    }

    @Override
    public void createCategory(Category category) {
//        category.setCategoryId(nextId++);
        Category savedCategory = categoryRepository.findByCategoryName(category.getCategoryName());
        if(savedCategory==null) {
            categoryRepository.save(category);
        }
        else{
            throw new APIException("Category with name: "+ category.getCategoryName()+ " already exist..!!");
        }
    }

    @Override
    public String deleteCategory(Long categoryId){
        Optional<Category> optCategory = categoryRepository.findById(categoryId);
        // Default exception
        //Category category = optCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        // Custom exception
        Category category = optCategory.orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(category);
        return "category deleted with id: "+categoryId;
    }

    @Override
    public Category updateCategory(Category category, Long categoryId){
//        Category savedCategory = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));
        // Custom exception
           Category savedCategory = categoryRepository.findById(categoryId)
                           .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return savedCategory;
    }
}