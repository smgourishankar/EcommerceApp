package com.ecommerce.projects.Service;

import com.ecommerce.projects.Payload.CategoryDTO;
import com.ecommerce.projects.Payload.CategoryResponse;
import com.ecommerce.projects.exceptions.APIException;
import com.ecommerce.projects.exceptions.ResourceNotFoundException;
import com.ecommerce.projects.model.Category;
import com.ecommerce.projects.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService{

//    private List<Category> categories = new ArrayList<>();
//    private Long nextId= 1L;

    @Autowired
    private CategoryRepository categoryRepository;

    // getting model mapper object here
    @Autowired
    private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
//Pageable : It is an interface, it represents the request for a
           //specific page of data from the database query results.

        // Providing sorting for our entities in our application:-------
        Sort sortByAndOrder = sortOrder.equalsIgnoreCase("ascending") ?
                Sort.by(sortBy).ascending() :Sort.by(sortBy).descending();

        // Applying pagination to out app :---------
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByAndOrder);
        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();
        if(!categories.isEmpty()){
            List<CategoryDTO> categoryDTOS = categories.stream()
                    .map(category -> modelMapper.map(category, CategoryDTO.class))
                    .toList();
            CategoryResponse categoryResponse = new CategoryResponse();
            categoryResponse.setContent(categoryDTOS);
            categoryResponse.setPageNumber(categoryPage.getNumber());
            categoryResponse.setPageSize(categoryPage.getSize());
            categoryResponse.setTotalElements(categoryPage.getTotalElements());
            categoryResponse.setTotalPages(categoryPage.getTotalPages());
            categoryResponse.setLastPage(categoryPage.isLast());
            return categoryResponse;
        }
        else{
            throw new APIException("Categories not created till now..!!");
        }
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO categoryDTO) {
//        category.setCategoryId(nextId++);
        Category category = modelMapper.map(categoryDTO, Category.class);
        Category categoryFromDb = categoryRepository.findByCategoryName(category.getCategoryName());
        if(categoryFromDb==null) {
            Category savedCategory = categoryRepository.save(category);
            return modelMapper.map(savedCategory, CategoryDTO.class);
        }
        else{
            throw new APIException("Category with name: "+ category.getCategoryName()+ " already exist..!!");
        }
    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId){
        Optional<Category> optCategory = categoryRepository.findById(categoryId);
        // Default exception
        //Category category = optCategory.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Category not found"));
        // Custom exception
        Category category = optCategory.orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        categoryRepository.delete(category);
        return modelMapper.map(category, CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(CategoryDTO categoryDTO, Long categoryId){
//        Category savedCategory = categoryRepository.findById(categoryId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Resource not found"));
        // Custom exception
           Category savedCategory = categoryRepository.findById(categoryId)
                           .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));
        Category category = modelMapper.map(categoryDTO, Category.class);
        category.setCategoryId(categoryId);
        savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryDTO.class);
    }
}