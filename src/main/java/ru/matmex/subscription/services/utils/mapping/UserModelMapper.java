package ru.matmex.subscription.services.utils.mapping;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.user.UserModel;

import java.util.function.Function;

@Component
public class UserModelMapper implements Function<User, UserModel> {
    CategoryModelMapper categoryModelMapper;
    SubscriptionModelMapper subscriptionModelMapper;

    UserModelMapper(CategoryModelMapper categoryModelMapper, SubscriptionModelMapper subscriptionModelMapper) {
        this.categoryModelMapper = categoryModelMapper;
        this.subscriptionModelMapper = subscriptionModelMapper;
    }

    @Override
    public UserModel apply(User user) {
        return new UserModel(
                user.getId(),
                user.getUsername(),
                user.getSubscriptions().stream().map(subscriptionModelMapper).toList(),
                user.getCategories().stream().map(categoryModelMapper).toList());
    }

    public UserModel build(User user) {
        return apply(user);
    }
}
