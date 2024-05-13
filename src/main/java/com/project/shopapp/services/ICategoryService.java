package com.project.shopapp.services;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;

import java.util.List;

public interface ICategoryService {

    Category createCategory(CategoryDTO categoryDTO);

    Category getCategoryById(long id);  // long ko cho phep null, Long thi van cho nhe

    List<Category> getAllCategories();

    Category updateCategoryById(long id, CategoryDTO categoryDTO);

    void deleteCategory(long id);
}
