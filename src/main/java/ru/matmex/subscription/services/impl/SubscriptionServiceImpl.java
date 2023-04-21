package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.SubscriptionModel;
import ru.matmex.subscription.repositories.SubscriptionRepository;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.utils.MappingUtils;

import java.util.List;
import java.util.Objects;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<SubscriptionModel> getSubscriptions(User user) {
        return getSubscriptionsByUser(user).stream().map(MappingUtils::mapToSubscriptionModel).toList();
    }

    public List<Subscription> getSubscriptionsByUser(User user) {
        return subscriptionRepository.findSubscriptionByUser(user).orElseThrow(EntityNotFoundException::new);
    }

    public SubscriptionModel getSubscription(User user, String name) {
        return getSubscriptions(user).stream().filter(sub -> Objects.equals(sub.getName(), name)).findFirst().orElseThrow();
    }

    @Override
    public Subscription createSubscription(SubscriptionModel subscription) {
        return subscriptionRepository.save(MappingUtils.mapToSubscriptionEntity(subscription));
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public Subscription updateSubscription(SubscriptionModel subscriptionModel) {
        return null;
        //TODO
    }
}
