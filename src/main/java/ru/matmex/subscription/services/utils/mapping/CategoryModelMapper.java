package ru.matmex.subscription.services.utils.mapping;

import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.models.category.CategoryModel;

import java.util.Objects;

/**
 * Преобразование сущности категории в модель категории
 */
@Component
public class CategoryModelMapper {

    public CategoryModel map(Category category) {
        return new CategoryModel(category.getId(),
                category.getName(),
                category.getSubscriptions()
                        .stream()
                        .filter(x -> Objects.equals(x.getUser().getId(), category.getUser().getId()))
                        .toList());
    }
}
