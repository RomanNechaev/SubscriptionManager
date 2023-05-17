package ru.matmex.subscription.models.subscription;

import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.user.UserModel;

import java.util.Date;

public record SubscriptionModel(
        Long id,
        String name,
        Date paymentDate,
        Double price,
        String category) {
}
