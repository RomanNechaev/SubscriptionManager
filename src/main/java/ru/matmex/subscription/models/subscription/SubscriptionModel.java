package ru.matmex.subscription.models.subscription;

import java.util.Date;

public record SubscriptionModel(
        Long id,
        String name,
        Date paymentDate,
        Double price,
        String category) {
}
