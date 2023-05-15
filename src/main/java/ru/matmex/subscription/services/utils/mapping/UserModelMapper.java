package ru.matmex.subscription.services.utils.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;

import java.util.function.Function;

/**
 * Преобразование сущности пользователя в DTO
 */
@Component
public class UserModelMapper implements Function<User, UserModel> {
    CategoryModelMapper categoryModelMapper;
    SubscriptionModelMapper subscriptionModelMapper;

    @Autowired
    UserModelMapper(CategoryModelMapper categoryModelMapper, SubscriptionModelMapper subscriptionModelMapper) {
        this.categoryModelMapper = categoryModelMapper;
        this.subscriptionModelMapper = subscriptionModelMapper;
    }

    @Override
    public UserModel apply(User user) {
        return new UserModel(
                user.getId(),
                user.getUsername(),
                user.getSubscriptions()
                        .stream()
                        .map(subscriptionModelMapper)
                        .toList(),
                user.getCategories()
                        .stream()
                        .map(categoryModelMapper).toList());
    }

    public UserModel build(User user) {
        return apply(user);
    }
}
