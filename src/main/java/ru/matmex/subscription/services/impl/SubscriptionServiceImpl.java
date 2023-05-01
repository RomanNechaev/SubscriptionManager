package ru.matmex.subscription.services.impl;

import jakarta.persistence.Access;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;
import ru.matmex.subscription.repositories.SubscriptionRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.Parser;
import ru.matmex.subscription.services.utils.mapping.SubscriptionModelMapper;

import java.util.List;
import java.util.Objects;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;
    CategoryService categoryService;
    UserService userService;
    SubscriptionModelMapper subscriptionModelMapper;

    @Autowired
    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, SubscriptionModelMapper subscriptionModelMapper,
                                   CategoryService categoryService, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.subscriptionModelMapper = subscriptionModelMapper;
        this.categoryService = categoryService;
        this.userService = userService;
    }

    @Override
    public List<SubscriptionModel> getSubscriptions() {
        return getSubscriptionsByUser(userService.getCurrentUser()).stream().map(subscriptionModelMapper).toList();
    }

    public List<Subscription> getSubscriptionsByUser(User user) {
        return subscriptionRepository.findSubscriptionByUser(user).orElseThrow(EntityNotFoundException::new);
    }

    public SubscriptionModel getSubscription(String name) {
        return getSubscriptions().stream().filter(sub -> Objects.equals(sub.name(), name)).findFirst().orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public SubscriptionModel createSubscription(CreateSubscriptionModel createSubscriptionModel) {
        Subscription subscription = new Subscription(
                createSubscriptionModel.name(),
                createSubscriptionModel.price(),
                Parser.parseToDate(createSubscriptionModel.paymentDate()),
                categoryService.getCategory(createSubscriptionModel.category()),
                userService.getCurrentUser()
        );
        subscriptionRepository.save(subscription);
        return subscriptionModelMapper.build(subscription);
    }

    @Override
    public void deleteSubscription(Long id) {
        if (subscriptionRepository.existsById(id))
            subscriptionRepository.deleteById(id);
        else throw new EntityNotFoundException();
    }

    @Override
    public SubscriptionModel updateSubscription(UpdateSubscriptionModel updateSubscriptionModel) {
        Subscription subscription = subscriptionRepository.findById(updateSubscriptionModel.id()).orElseThrow(EntityNotFoundException::new);
        subscription.setCategory(categoryService.getCategory(updateSubscriptionModel.category()));
        subscription.setName(updateSubscriptionModel.name());
        subscription.setPrice(updateSubscriptionModel.price());
        subscription.setPaymentDate(Parser.parseToDate(updateSubscriptionModel.paymentDate()));
        subscriptionRepository.save(subscription);
        return subscriptionModelMapper.build(subscription);
    }
}
