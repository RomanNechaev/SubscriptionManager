package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;
import ru.matmex.subscription.models.category.UpdateCategoryModel;
import ru.matmex.subscription.repositories.CategoryRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;

import java.util.Collections;
import java.util.List;

@Component
public class CategoryServiceImpl implements CategoryService {
    CategoryRepository categoryRepository;
    SubscriptionService subscriptionService;
    UserService userService;

    @Override
    public List<CategoryModel> getCategories() {
        return null;
    }

    @Override
    public CategoryModel getCategory(String name) {
        return null;
    }

    public List<Subscription> getCategoryByUser(User user) {
       return null;
    }

    @Override
    public void create(CreateCategoryModel createCategoryModel) {

    }

    @Override
    public UpdateCategoryModel addSubscription(UpdateCategoryModel updateCategoryModel) {
        return null;
    }

    @Override
    public void deleteSubscription(Long id) {

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
        if (categoryRepository.existsById(id)) {
            categoryRepository
                    .findAllById(Collections.singleton(id))
                    .forEach(subscription -> subscriptionService.deleteSubscription(subscription.getId())
            );
            categoryRepository.deleteAllById(Collections.singleton(id));
        }
        else throw new EntityNotFoundException();
    }
}
