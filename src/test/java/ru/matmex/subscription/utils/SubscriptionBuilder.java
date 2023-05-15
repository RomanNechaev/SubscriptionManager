package ru.matmex.subscription.utils;

import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;

import java.util.Date;
/**
 * Создание объекта подписки
 */
public class SubscriptionBuilder {
    private String name;
    private Double price;
    private Date paymentDate;
    private Category category;
    private User user;

    private SubscriptionBuilder() {
    }

    public static SubscriptionBuilder anSubscription() {
        return new SubscriptionBuilder();
    }

    public SubscriptionBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public SubscriptionBuilder withPrice(Double price) {
        this.price = price;
        return this;
    }

    public SubscriptionBuilder withPaymentDate(Date date) {
        this.paymentDate = date;
        return this;
    }

    public SubscriptionBuilder withCategory(Category category) {
        this.category = category;
        return this;
    }

    public SubscriptionBuilder withUser(User user) {
        this.user = user;
        return this;
    }

    public Subscription build() {
        return new Subscription(name, price, paymentDate, category, user);
    }

    public Subscription defaultSubscription() {
        return SubscriptionBuilder.anSubscription()
                .withName("test")
                .withPrice(12.0)
                .withPaymentDate(new Date(1, 2, 3))
                .withCategory(CategoryBuilder.anCategory().defaultCategory())
                .withUser(UserBuilder.anUser().defaultUser())
                .build();
    }

}
