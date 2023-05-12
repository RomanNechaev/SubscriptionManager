package ru.matmex.subscription.utils;

import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;

import java.util.ArrayList;
import java.util.List;

public class CategoryBuilder {

    private String name;
    private List<Subscription> subscriptions;

    private User user;

    private CategoryBuilder() {
    }

    public static CategoryBuilder anCategory() {
        return new CategoryBuilder();
    }

    public CategoryBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public CategoryBuilder withSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
        return this;
    }

    public CategoryBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Category build() {
        return new Category(name, subscriptions, user);
    }

    public Category defaultCategory() {
        return new Category("test", new ArrayList<>(), UserBuilder.anUser().defaultUser());
    }
}
