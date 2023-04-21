package ru.matmex.subscription.services;

import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.SubscriptionModel;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionModel> getSubscriptions(User user);

    SubscriptionModel getSubscription(User user, String name);

    Subscription createSubscription(SubscriptionModel subscriptionModel);

    void deleteSubscription(Long id);

    Subscription updateSubscription(SubscriptionModel subscriptionModel);

}
