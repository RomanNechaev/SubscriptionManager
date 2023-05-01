package ru.matmex.subscription.services.utils;

import org.springframework.stereotype.Component;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.services.CategoryService;
import ru.matmex.subscription.services.UserService;

@Component
public class MappingUtils {
    private final CategoryService categoryService;
    private final UserService userService;

    public MappingUtils(CategoryService categoryService, UserService userService) {
        this.categoryService = categoryService;
        this.userService = userService;

    }

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

    public Subscription mapToSubscriptionEntity(CreateSubscriptionModel createSubscriptionModel) {
        return new Subscription(
                createSubscriptionModel.getName(),
                createSubscriptionModel.getPrice(),
                Parser.parseToDate(createSubscriptionModel.getPaymentDate()),
                mapToCategoryEntity(categoryService.createIfNotExists(createSubscriptionModel.getCategory())),
                userService.getCurrentUser()
        );
    }

    public User mapToUserEntity(UserModel userModel) {
        return null;
        //TODO
    }

    public static User mapToUserEntity(UserRegistrationModel userModel) {
        return new User(userModel.getUsername(), userModel.getEmail(), userModel.getPassword());
    }

    public Category mapToCategoryEntity(CategoryModel categoryModel) {
        return null;
        //TODO
    }

}
