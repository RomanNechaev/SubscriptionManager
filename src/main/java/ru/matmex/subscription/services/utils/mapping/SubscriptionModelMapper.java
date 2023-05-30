package ru.matmex.subscription.services.utils.mapping;

import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.models.subscription.SubscriptionModel;

/**
 * Преобразование сущности подписки в модель подписки
 */
@Component
public class SubscriptionModelMapper {
    public SubscriptionModel map(Subscription subscription) {
        return new SubscriptionModel(
                subscription.getId(),
                subscription.getName(),
                subscription.getPaymentDate(),
                subscription.getPrice(),
                subscription.getCategory().getName());
    }
}
