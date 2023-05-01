package ru.matmex.subscription.services.impl;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;
import ru.matmex.subscription.services.CategoryService;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Override
    public List<CategoryModel> getCategories() {
        return null;
    }

    @Override
    public Category getCategory(String name) {
        return null;
    }

    @Override
    public void create(CreateCategoryModel createCategoryModel) {

    }

    @Override
    public CategoryModel createIfNotExists(CreateCategoryModel createCategoryModel) {
        return null;
    }

    @Override
    public CategoryModel createIfNotExists(String name) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
