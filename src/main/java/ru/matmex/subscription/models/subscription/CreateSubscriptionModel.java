package ru.matmex.subscription.models.subscription;

import java.util.Date;

public class CreateSubscriptionModel {
    private String name;
    private String paymentDate;
    private Double price;
    private String category;

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public Double getPrice() {
        return price;
    }
}
