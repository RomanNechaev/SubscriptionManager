package ru.matmex.subscription.models.subscription;

import ru.matmex.subscription.models.category.CategoryModel;
import ru.matmex.subscription.models.user.UserModel;

import java.util.Date;

public class SubscriptionModel {
    private Long id;
    private String name;

    private Date paymentDate;
    private Double price;
    private CategoryModel category;
    private UserModel user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public void setCategory(CategoryModel category) {
        this.category = category;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }
}
