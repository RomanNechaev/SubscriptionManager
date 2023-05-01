package ru.matmex.subscription.models.category;

import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.user.UserModel;

import java.util.List;

public record CategoryModel(Long id, String name, List<SubscriptionModel> subscriptions, UserModel user) {}