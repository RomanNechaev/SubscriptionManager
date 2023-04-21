package ru.matmex.subscription.models;

import java.util.List;

public class CategoryModel {
    private String name;
    private UserModel user;
    private Long id;
    private List<SubscriptionModel> subscriptions;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<SubscriptionModel> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<SubscriptionModel> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
