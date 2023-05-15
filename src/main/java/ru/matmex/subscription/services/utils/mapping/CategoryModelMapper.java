package ru.matmex.subscription.services.utils.mapping;

import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.models.category.CategoryModel;

import java.util.Objects;
import java.util.function.Function;

/**
 * Преобразование сущности категории в DTO
 */
@Component
public class CategoryModelMapper implements Function<Category, CategoryModel> {

    @Override
    public CategoryModel apply(Category category) {
        return new CategoryModel(category.getId(),
                category.getName(),
                category.getSubscriptions()
                        .stream()
                        .filter(x -> Objects.equals(x.getUser().getId(), category.getUser().getId()))
                        .toList());
    }
}
