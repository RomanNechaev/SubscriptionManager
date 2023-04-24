package ru.matmex.subscription.models.subscription;
public record CreateSubscriptionModel(String name, String paymentDate, Double price, String category) {
}
