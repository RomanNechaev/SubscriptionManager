package ru.matmex.subscription.models.user;
import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;

import java.util.List;

public class UserModel {
    private Long id;
    private String username;
    private List<SubscriptionModel> subscriptions;
    private List<CategoryModel> categories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<SubscriptionModel> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionModel> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public List<CategoryModel> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryModel> categories) {
        this.categories = categories;
    }
}
