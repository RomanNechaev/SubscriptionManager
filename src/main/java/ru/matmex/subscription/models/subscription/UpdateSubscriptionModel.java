package ru.matmex.subscription.models.subscription;

public record UpdateSubscriptionModel(Long id, String paymentDate, String name, Double price, String category) {}
