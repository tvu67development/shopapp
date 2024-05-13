package com.project.shopapp.controllers;

import com.project.shopapp.dtos.CategoryDTO;
import com.project.shopapp.models.Category;
import com.project.shopapp.services.CategoryService;
import com.project.shopapp.services.ICategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final ICategoryService iCategoryService;

    @PostMapping
    public ResponseEntity<?> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result
    ) {
        if (result.hasErrors()) {
            List<String> errorMessages = result.getFieldErrors()
                    .stream()
                    .map(FieldError::getDefaultMessage)
                    .toList();
            return ResponseEntity.badRequest().body(errorMessages);
        }
        iCategoryService.createCategory(categoryDTO);
        return ResponseEntity.ok("Create Category successfully");
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
    public ResponseEntity<String> updateCategory(@PathVariable("category_id") long id,
                                                 @Valid @RequestBody CategoryDTO categoryDTO, BindingResult result) {
        iCategoryService.updateCategoryById(id, categoryDTO);
        return ResponseEntity.ok("Update Category by Id " + id + " successfully");
    }

    @DeleteMapping("/{category_id}")
    public ResponseEntity<String> deleteCategory(@PathVariable("category_id") Long id) {
        iCategoryService.deleteCategory(id);
        return ResponseEntity.ok("Delete Category by Id " + id + " successfully");
    }
}
