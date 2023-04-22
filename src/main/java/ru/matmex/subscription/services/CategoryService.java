package ru.matmex.subscription.services;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;

import java.util.List;

public interface CategoryService {
   List<CategoryModel> getCategories();

   CategoryModel getCategory(String name);

   void create(CreateCategoryModel createCategoryModel);

   CategoryModel createIfNotExists(CreateCategoryModel createCategoryModel);
   CategoryModel createIfNotExists(String name);
   void delete(Long id);


}
