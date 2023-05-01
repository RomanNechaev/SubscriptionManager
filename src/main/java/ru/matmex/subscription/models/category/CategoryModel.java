package ru.matmex.subscription.models.category;

import ru.matmex.subscription.models.subscription.SubscriptionModel;

import java.util.List;

public record CategoryModel(String name, Long id, List<SubscriptionModel> subscriptions) {
}