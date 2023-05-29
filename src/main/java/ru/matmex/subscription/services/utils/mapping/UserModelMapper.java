package ru.matmex.subscription.services.utils.mapping;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.user.UserModel;

/**
 * Преобразование сущности пользователя в модель пользователя
 */
@Component
public class UserModelMapper {
    CategoryModelMapper categoryModelMapper;
    SubscriptionModelMapper subscriptionModelMapper;

    @Autowired
    public UserModelMapper(CategoryModelMapper categoryModelMapper, SubscriptionModelMapper subscriptionModelMapper) {
        this.categoryModelMapper = categoryModelMapper;
        this.subscriptionModelMapper = subscriptionModelMapper;
    }

    public UserModel map(User user) {
        return new UserModel(
                user.getId(),
                user.getUsername(),
                user.getCategories()
                        .stream()
                        .map(x -> categoryModelMapper.map(x)).toList());
    }
}
