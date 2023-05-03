package ru.matmex.subscription.models.category;

import ru.matmex.subscription.entities.Subscription;

import java.util.List;

public record CategoryModel(Long id, String name, List<Subscription> subscriptions) {}