package ru.matmex.subscription.services;

import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;

import java.util.List;

public interface SubscriptionService {
    List<SubscriptionModel> getSubscriptions();

    SubscriptionModel getSubscription(String name);

    List<Subscription> getSubscriptionsByUser(User user);

    SubscriptionModel createSubscription(CreateSubscriptionModel createSubscriptionModel);

    String deleteSubscription(Long id);

    SubscriptionModel updateSubscription(UpdateSubscriptionModel updateSubscriptionModel);

}
