package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;
import ru.matmex.subscription.models.category.UpdateCategoryModel;
import ru.matmex.subscription.services.CategoryService;

import java.util.List;

/**
 * Контроллер для операций с категориями
 */
@Controller
public class CategoryController {
    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Создать категорию
     *
     * @param createCategoryModel - данные о категории, заполненные пользователем на клиенте
     * @return HTTP ответ о созданной категории
     */
    @PostMapping(value = "/api/app/category")
    public ResponseEntity<CategoryModel> create(@RequestBody CreateCategoryModel createCategoryModel) {
        return ResponseEntity.ok(categoryService.create(createCategoryModel));
    }

    /**
     * Получить категорию по имени
     *
     * @param name имя категории
     * @return HTTP ответ с информацией о категории
     */
    @GetMapping(value = "/api/app/category/{name}")
    public ResponseEntity<CategoryModel> getCategoryByName(@PathVariable String name) {
        return ResponseEntity.ok(categoryService.getCategoryToClient(name));
    }

    /**
     * Получить категории текущего пользователя
     *
     * @return HTTP ответ с информацией о категориях
     */
    @GetMapping(value = "/api/app/categories")
    public ResponseEntity<List<CategoryModel>> getCategoriesByCurrentUser() {
        return ResponseEntity.ok(categoryService.getCategoriesByCurrentUsername());
    }

    /**
     * Обновить категорию
     *
     * @param updateSubscriptionModel данные об обновленной категории, заполненные пользователем на клиенте
     * @return HTTP ответ с информацией об обноленной категории
     */
    @PutMapping(value = "/api/app/category")
    public ResponseEntity<CategoryModel> update(@RequestBody UpdateCategoryModel updateSubscriptionModel) {
        return ResponseEntity.ok(categoryService.update(updateSubscriptionModel));
    }

    /**
     * Удалить категорию
     *
     * @param id индефикатор категории в БД
     * @return HTTP ответ об удалении
     */
    @DeleteMapping(value = "/api/app/category/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.delete(id));
    }

}
