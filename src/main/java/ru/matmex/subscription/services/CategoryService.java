package ru.matmex.subscription.services;

import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;
import ru.matmex.subscription.models.category.UpdateCategoryModel;

import java.util.List;

public interface CategoryService {
    List<CategoryModel> getCategoriesByCurrentUsername();

    Category getCategory(String name);

    CategoryModel getCategoryToClient(String name);

    CategoryModel create(CreateCategoryModel createCategoryModel);

    void createDefaultSubscription(User user);

    CategoryModel update(UpdateCategoryModel updateCategoryModel);


    String delete(Long id);


}
