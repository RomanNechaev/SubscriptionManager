package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ContextConfiguration;
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
import ru.matmex.subscription.utils.CategoryBuilder;
import ru.matmex.subscription.utils.UserBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
@ContextConfiguration(classes = {CategoryServiceImpl.class})
@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserService userService;
    private final CategoryModelMapper categoryModelMapper = new CategoryModelMapper();
    @Mock
    private SubscriptionService subscriptionService;

    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(
                categoryRepository,
                userService,
                categoryModelMapper,
                subscriptionService);
    }

    /**
     * Тестирование получения категорий текущего пользователя пользователя
     */
    @Test
    void testCanGetCategoriesByCurrentUsername() {
        User testUser = UserBuilder.anUser().defaultUser();
        List<Category> categories = List.of(
                CategoryBuilder.anCategory().defaultCategory(),
                CategoryBuilder.anCategory()
                        .withName("test2")
                        .withSubscriptions(new ArrayList<>())
                        .withUser(UserBuilder.anUser().defaultUser())
                        .build()
        );

        when(userService.getCurrentUser()).thenReturn(testUser);

        when(categoryRepository.findCategoriesByUser(testUser)).thenReturn(Optional.of(categories));

        List<CategoryModel> categoryModels = categoryService.getCategoriesByCurrentUsername();

        assertThat(categories.stream().map(categoryModelMapper).toList()).isEqualTo(categoryModels);

        verify(categoryRepository).findCategoriesByUser(testUser);
    }

    /**
     * Тестирование получения категорий пользователя
     */
    @Test
    void testCanGetCategoryByName() {
        String categoryName = "test";
        Category testCategory = CategoryBuilder.anCategory().defaultCategory();

        when(categoryRepository.findCategoryByName(categoryName)).thenReturn(Optional.of(testCategory));

        Category actualCategory = categoryService.getCategory(categoryName);

        assertThat(actualCategory).isEqualTo(testCategory);

        verify(categoryRepository).findCategoryByName(categoryName);
    }

    /**
     * Тестирование создания категории
     */
    @Test
    void testCanCreateCategory() {
        CreateCategoryModel createCategoryModel = new CreateCategoryModel("test");
        given(categoryRepository.existsByName("test")).willReturn(true);
        categoryService.create(createCategoryModel);

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository, times(1)).save(categoryArgumentCaptor.capture());

        Category category = categoryArgumentCaptor.getValue();

        assertThat(category.getName()).isSameAs(createCategoryModel.name());

    }

    /**
     * Тестирвоание создание категории по умолчанию
     */
    @Test
    void testCanCreateDefaultCategory() {
        User testUser = UserBuilder.anUser().defaultUser();
        categoryService.createDefaultSubscription(testUser);

        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);

        verify(categoryRepository, times(1)).save(categoryArgumentCaptor.capture());

        Category category = categoryArgumentCaptor.getValue();

        assertThat(category.getName()).isSameAs("default");

    }

    /**
     * Тестирование создания категории, если она уже существует
     */
    @Test
    void testWillThrowWhenCreateCategoryExists() {
        CreateCategoryModel createCategoryModel = new CreateCategoryModel("test");
        given(categoryRepository.existsByName("test")).willReturn(false);

        assertThatThrownBy(() -> categoryService.create(createCategoryModel))
                .isInstanceOf(EntityExistsException.class)
                .hasMessageContaining("Сущность с именем" + createCategoryModel.name() + "уже существует");

        verify(categoryRepository, never()).save(any());
    }

    /**
     * Тестирование обновления категории
     */
    @Test
    void testCanUpdateCategory() {
        String newName = "sport";
        String oldName = "life";
        UpdateCategoryModel updateCategoryModel = new UpdateCategoryModel(
                12L,
                newName
        );
        Category category = CategoryBuilder.anCategory()
                .withName(oldName)
                .withSubscriptions(new ArrayList<>())
                .withUser(UserBuilder.anUser().defaultUser())
                .build();

        when(categoryRepository.findCategoryById(12L)).thenReturn(Optional.of(category));
        categoryService.update(updateCategoryModel);

        String updatedName = category.getName();

        assertThat(updatedName).isSameAs(newName);
    }

    /**
     * Тестирование удаления категории
     */
    @Test
    void testCanDeleteCategory() {
        Long categoryId = 12L;
        given(categoryRepository.existsById(categoryId)).willReturn(true);

        categoryService.delete(categoryId);

        verify(categoryRepository).deleteAllById(Collections.singleton(categoryId));

    }

    /**
     * Тестирование удаления несуществующей категории
     */
    @Test
    void testWillThrownWhenDeleteCategoryNotFound() {
        Long categoryId = 12L;
        given(categoryRepository.existsById(categoryId)).willReturn(false);

        assertThatThrownBy(() -> categoryService.delete(categoryId))
                .isInstanceOf(EntityNotFoundException.class);
    }

    /**
     * Тестирование преобразования сущности категории в DTO
     */
    @Test
    void testCanMapToCategoryModel() {
        Category category = CategoryBuilder.anCategory().defaultCategory();
        when(categoryRepository.findCategoryByName(category.getName())).thenReturn(Optional.of(category));
        CategoryModel categoryModel = categoryService.getCategoryToClient(category.getName());

        assertThat(categoryModel).
                hasFieldOrPropertyWithValue("name", "test");
    }


}