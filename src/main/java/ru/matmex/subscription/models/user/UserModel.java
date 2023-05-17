package ru.matmex.subscription.models.user;

import ru.matmex.subscription.models.category.CategoryModel;

import java.util.List;

public record UserModel(Long id, String username,
                        List<CategoryModel> categories) {
}
