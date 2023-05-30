package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.Category;
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

/**
 * Реализация сервиса для операций с категориями
 */
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

    /**
     * Получить категорию текущего юзера
     *
     * @return Список категорий текущего пользователя
     */
    @Override
    public List<CategoryModel> getCategoriesByCurrentUsername() {
        return categoryRepository
                .findCategoriesByUser(userService.getCurrentUser())
                .orElseThrow(EntityNotFoundException::new)
                .stream().map(categoryModelMapper)
                .toList();
    }

    /**
     * Получить категорию по имени
     *
     * @param name Имя категории
     * @return категория с именем name
     */
    @Override
    public Category getCategory(String name) {
        return categoryRepository
                .findCategoryByNameAndUser(name, userService.getCurrentUser())
                .orElseThrow(EntityNotFoundException::new);
    }

    /**
     * Преобразовать сущность категории в модель категории
     *
     * @param name имя категори
     * @return модель категории
     */
    @Override
    public CategoryModel getCategoryToClient(String name) {
        return categoryModelMapper.apply(getCategory(name));
    }

    /**
     * Создать категорию
     *
     * @param createCategoryModel - данные, заполненные пользователем при создании категории на клиенте
     * @return модель категории
     */
    @Override
    public CategoryModel create(CreateCategoryModel createCategoryModel) {
        if (categoryRepository.existsByNameAndUser(createCategoryModel.name(), userService.getCurrentUser())) {
            throw new EntityExistsException("Сущность с именем" + createCategoryModel.name() + "уже существует");
        }
        Category category = new Category(createCategoryModel.name(), new ArrayList<>(), userService.getCurrentUser());
        categoryRepository.save(category);
        return categoryModelMapper.apply(category);
    }

    /**
     * Создать категорию по умолчанию
     *
     * @param user - Сущность юзера
     */
    @Override
    public void createDefaultSubscription(User user) {
        Category category = new Category("default", new ArrayList<>(), user);
        categoryRepository.save(category);
    }

    /**
     * Обновить категорию
     *
     * @param updateCategoryModel параметры, заполненные пользователем на клиенте
     * @return модель категории
     */
    @Override
    public CategoryModel update(UpdateCategoryModel updateCategoryModel) {
        Category category = categoryRepository
                .findCategoryById(updateCategoryModel.id())
                .orElseThrow(EntityNotFoundException::new);//TODO
        category.setName(updateCategoryModel.name());
        categoryRepository.save(category);
        return categoryModelMapper.apply(category);
    }

    /**
     * Удалить категорию
     *
     * @param id - индетификатор категории в БД
     * @return Сообщение об успешном удалении, в случае если не произошла ошибка
     */
    @Override
    public String delete(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository
                    .findCategoryById(id).orElseThrow().getSubscriptions()
                    .forEach(subscription -> subscriptionService.deleteSubscription(subscription.getId())
                    );
            categoryRepository.deleteAllById(Collections.singleton(id));
            return "Категория успешна удалена!";
        } else throw new EntityNotFoundException();
    }
}
