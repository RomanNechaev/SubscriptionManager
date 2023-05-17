package ru.matmex.subscription.models.subscription;

public record UpdateSubscriptionModel(Long id, String name, String paymentDate, Double price, String category) {
}
