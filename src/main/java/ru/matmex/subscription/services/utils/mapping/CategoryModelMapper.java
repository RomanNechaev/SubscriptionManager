package ru.matmex.subscription.services.utils.mapping;

import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.models.category.CategoryModel;

import java.util.function.Function;

@Component
public class CategoryModelMapper implements Function<Category, CategoryModel> {

    @Override
    public CategoryModel apply(Category category) {
        return new CategoryModel(category.getId(),
                category.getName(),
                category.getSubscriptions()
        );
    }
}
