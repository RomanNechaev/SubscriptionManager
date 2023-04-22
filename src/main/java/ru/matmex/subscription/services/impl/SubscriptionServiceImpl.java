package ru.matmex.subscription.services.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;
import ru.matmex.subscription.repositories.SubscriptionRepository;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.MappingUtils;
import ru.matmex.subscription.services.utils.Parser;

import java.util.List;
import java.util.Objects;

@Component
public class SubscriptionServiceImpl implements SubscriptionService {
    SubscriptionRepository subscriptionRepository;
    CategoryService categoryService;
    UserService userService;
    private MappingUtils mapper;

    public SubscriptionServiceImpl(SubscriptionRepository subscriptionRepository, CategoryService categoryService, UserService userService) {
        this.subscriptionRepository = subscriptionRepository;
        this.mapper = new MappingUtils(categoryService, userService);
    }

    @Override
    public List<SubscriptionModel> getSubscriptions() {
        return getSubscriptionsByUser(userService.getCurrentUser()).stream().map(MappingUtils::mapToSubscriptionModel).toList();
    }

    public List<Subscription> getSubscriptionsByUser(User user) {
        return subscriptionRepository.findSubscriptionByUser(user).orElseThrow(EntityNotFoundException::new);
    }

    public SubscriptionModel getSubscription(String name) {
        return getSubscriptions().stream().filter(sub -> Objects.equals(sub.getName(), name)).findFirst().orElseThrow(EntityNotFoundException::new);
    }

    @Override
    public SubscriptionModel createSubscription(CreateSubscriptionModel createSubscriptionModel) {
        return MappingUtils.mapToSubscriptionModel(subscriptionRepository.save(mapper.mapToSubscriptionEntity(createSubscriptionModel)));
    }

    @Override
    public void deleteSubscription(Long id) {
        if (subscriptionRepository.existsById(id))
            subscriptionRepository.deleteById(id);
        else throw new EntityNotFoundException();
    }

    @Override
    public SubscriptionModel updateSubscription(UpdateSubscriptionModel updateSubscriptionModel) {
        Subscription subscription = subscriptionRepository.findById(updateSubscriptionModel.getId()).orElseThrow(EntityNotFoundException::new);
        subscription.setCategory(mapper.mapToCategoryEntity(categoryService.getCategory(updateSubscriptionModel.getCategory())));
        subscription.setName(updateSubscriptionModel.getName());
        subscription.setPrice(updateSubscriptionModel.getPrice());
        subscription.setPaymentDate(Parser.parseToDate(updateSubscriptionModel.getPaymentDate()));
        subscriptionRepository.save(subscription);
        return MappingUtils.mapToSubscriptionModel(subscription);
    }
}
