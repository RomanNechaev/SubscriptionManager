package ru.matmex.subscription.services.utils.mapping;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.models.subscription.SubscriptionModel;

import java.util.function.Function;
@Component
public class SubscriptionModelMapper implements Function<Subscription, SubscriptionModel> {
    @Override
    public SubscriptionModel apply(Subscription subscription) {
        return new SubscriptionModel(
                subscription.getId(),
                subscription.getName(),
                subscription.getPaymentDate(),
                subscription.getPrice(),
                subscription.getCategory().getName());
    }

    public SubscriptionModel build(Subscription subscription) {
        return apply(subscription);
    }
}
