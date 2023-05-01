package ru.matmex.subscription.models.user;

import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;

import java.util.List;

public record UserModel(Long id, String username, List<SubscriptionModel> subscriptions,
                        List<CategoryModel> categories) {
}
