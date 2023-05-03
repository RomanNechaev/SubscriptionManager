package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;
import ru.matmex.subscription.models.category.UpdateCategoryModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;
import ru.matmex.subscription.services.CategoryService;

import java.util.List;

@Controller
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping(value = "/api/app/category")
    public ResponseEntity<CategoryModel> create(@RequestBody CreateCategoryModel createCategoryModel) {
        return ResponseEntity.ok(categoryService.create(createCategoryModel));
    }

    @GetMapping(value = "/api/app/category/{name}")
    public ResponseEntity<CategoryModel> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoryToClient(name));
    }

    @GetMapping(value = "/api/app/categories")
    public ResponseEntity<List<CategoryModel>> getCategoriesByCurrentUser() {
        return ResponseEntity.ok(categoryService.getCategoriesByCurrentUsername());
    }

    @PutMapping(value = "/api/app/category")
    public ResponseEntity<CategoryModel> update(@RequestBody UpdateCategoryModel updateSubscriptionModel) {
        return ResponseEntity.ok(categoryService.update(updateSubscriptionModel));
    }

    @DeleteMapping(value = "/api/app/category/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }

}
