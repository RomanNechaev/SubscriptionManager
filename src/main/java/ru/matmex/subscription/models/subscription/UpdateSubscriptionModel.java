package ru.matmex.subscription.models.subscription;

public class UpdateSubscriptionModel {
    private Long id;
    private String paymentDate;
    private String name;
    private Double price;
    private String Category;

    public Long getId() {
        return id;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public String getCategory() {
        return Category;
    }
}
