package ru.matmex.subscription.services;

import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;

import java.util.List;

public interface SubscriptionService {
    List<Subscription> getSubscriptions(User user);

    void createSubscription(Subscription subscription);

    void deleteSubscription(Long id);

    void updateSubscription(Long id);

}
