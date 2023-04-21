package ru.matmex.subscription.services.utils;

import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.CategoryModel;
import ru.matmex.subscription.models.SubscriptionModel;
import ru.matmex.subscription.models.UserModel;

@Component
public class MappingUtils {
    public static UserModel mapToUserModel(User userEntity) {
        UserModel userModel = new UserModel();
        userModel.setId(userEntity.getId());
        userModel.setUsername(userEntity.getUsername());
        return userModel;
    }

    public static SubscriptionModel mapToSubscriptionModel(Subscription subscription) {
        SubscriptionModel subscriptionModel = new SubscriptionModel();
        subscriptionModel.setId(subscription.getId());
        subscriptionModel.setName(subscription.getName());
        subscriptionModel.setCategory(mapToCategoryModel(subscription.getCategory()));
        subscriptionModel.setPrice(subscription.getPrice());
        subscriptionModel.setUser(mapToUserModel(subscription.getUser()));
        subscriptionModel.setPaymentDate(subscription.getPaymentDate());
        return subscriptionModel;
    }
    public static CategoryModel mapToCategoryModel(Category category) {
        CategoryModel categoryModel = new CategoryModel();
        categoryModel.setId(category.getId());
        categoryModel.setName(category.getName());
        categoryModel.setUser(mapToUserModel(category.getUser()));
        categoryModel.setSubscriptions(category.getSubscriptions().stream().map(MappingUtils::mapToSubscriptionModel).toList());
        return categoryModel;
    }

    public static Subscription mapToSubscriptionEntity(SubscriptionModel subscriptionModel) {
        return new Subscription(subscriptionModel.getName(),subscriptionModel.getPrice(),subscriptionModel.getPaymentDate()
                ,mapToCategoryEntity(subscriptionModel.getCategory()),
                mapToUserEntity(subscriptionModel.getUser()));
    }

    public static User mapToUserEntity(UserModel userModel) {
        return null;
        //TODO
    }

    public static Category mapToCategoryEntity(CategoryModel categoryModel) {
        return new Category(categoryModel.getName(),categoryModel.getSubscriptions().stream().map(MappingUtils::mapToSubscriptionEntity).toList(),mapToUserEntity(categoryModel.getUser()));
    }

}
