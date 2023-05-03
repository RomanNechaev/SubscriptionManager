package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.category.CreateCategoryModel;
import ru.matmex.subscription.models.category.UpdateCategoryModel;
import ru.matmex.subscription.repositories.CategoryRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.mapping.CategoryModelMapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final UserService userService;
    private final CategoryModelMapper categoryModelMapper;

    private final SubscriptionService subscriptionService;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               UserService userService,
                               CategoryModelMapper categoryModelMapper,
                               @Lazy SubscriptionService subscriptionService) {
        this.categoryRepository = categoryRepository;
        this.userService = userService;
        this.categoryModelMapper = categoryModelMapper;
        this.subscriptionService = subscriptionService;
    }

    @Override
    public List<CategoryModel> getCategoriesByCurrentUsername() {
        return categoryRepository
                .findCategoriesByUser(userService.getCurrentUser())
                .orElseThrow(EntityNotFoundException::new)
                .stream().map(categoryModelMapper)
                .toList();
    }

    @Override
    public Category getCategory(String name) {
        return categoryRepository.findCategoryByName(name).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public CategoryModel getCategoryToClient(String name) {
        return categoryModelMapper.apply(getCategory(name));
    }

    public List<Subscription> getCategoryByUser(User user) {
        return null;
    }

    @Override
    public CategoryModel create(CreateCategoryModel createCategoryModel) {
        Category category = new Category(createCategoryModel.name(), new ArrayList<>(), userService.getCurrentUser());
        categoryRepository.save(category);
        return categoryModelMapper.apply(category);
    }

    @Override
    public void createDefaultSubscription(User user) {
        Category category = new Category("default", new ArrayList<>(), user);
        categoryRepository.save(category);
    }

    @Override
    public CategoryModel update(UpdateCategoryModel updateCategoryModel) {
        Category category = categoryRepository.findCategoryById(updateCategoryModel.id()).orElseThrow(EntityNotFoundException::new);//TODO
        category.setName(updateCategoryModel.name());
        categoryRepository.save(category);
        return categoryModelMapper.apply(category);
    }

    @Override
    public String delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository
                    .findAllById(Collections.singleton(id))
                    .forEach(subscription -> subscriptionService.deleteSubscription(subscription.getId())
                    );
            categoryRepository.deleteAllById(Collections.singleton(id));
            return "Подписка успешна удалена!"; //TODO
        } else throw new EntityNotFoundException();
    }
}
