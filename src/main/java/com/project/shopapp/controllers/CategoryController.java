package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.responses.CategoryResponse;
import com.project.shopapp.services.ICategoryService;
import com.project.shopapp.components.LocalizationUtils;
import com.project.shopapp.utils.MessageKeys;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService iCategoryService;
    private final LocalizationUtils localizationUtils;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(
                    CategoryResponse.builder()
                            .message(localizationUtils.getLocalizeMessage(MessageKeys.INSERT_CATEGORY_FAILED))
                            .errors(errorMessages)
                            .category(new Category())
                            .build());
        }
        Category newCategory = iCategoryService.createCategory(categoryDTO);
        return ResponseEntity.ok(CategoryResponse.builder()
                        .message(localizationUtils.getLocalizeMessage(MessageKeys.INSERT_CATEGORY_SUCCESSFULLY))
                        .errors(new ArrayList<>())
                        .category(newCategory)
                        .build());
    }

    @GetMapping
    public ResponseEntity<List<Category>> getCategories(
            @RequestParam Long page,
            @RequestParam Long limit
    ) {
        List<Category> categories = iCategoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{category_id}")
    public ResponseEntity<CategoryResponse> updateCategory(@PathVariable("category_id") long id,
                                                                 @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        Category newCategory = iCategoryService.updateCategoryById(id, categoryDTO);
        return ResponseEntity.ok(
                CategoryResponse.builder()
                        .message(localizationUtils.getLocalizeMessage(MessageKeys.UPDATE_CATEGORY_SUCCESSFULLY, id))
                        .errors(new ArrayList<>())
                        .category(newCategory)
                        .build());
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("category_id") Long id) {
        iCategoryService.deleteCategory(id);
        return ResponseEntity.ok(localizationUtils.getLocalizeMessage(MessageKeys.DELETE_CATEGORY_SUCCESSFULLY, id));
    }
}
