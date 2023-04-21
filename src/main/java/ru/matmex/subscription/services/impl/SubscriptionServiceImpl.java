package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.repositories.SubscriptionRepository;
import ru.matmex.subscription.services.SubscriptionService;

import java.util.List;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public List<Subscription> getSubscriptions(User user) {
        return subscriptionRepository.findSubscriptionByUser(user).orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public void createSubscription(Subscription subscription) {
        subscriptionRepository.save(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        subscriptionRepository.deleteById(id);
    }

    @Override
    public void updateSubscription(Long id) {

    }
}
