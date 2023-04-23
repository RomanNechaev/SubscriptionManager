package ru.matmex.subscription.entities;

import jakarta.persistence.*;

import java.util.List;


@Entity
@Table(name = "Categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Category() {
    }

    public Category(Long id, String name, List<Subscription> subscriptions, User user) {
        this.id = id;
        this.name = name;
        this.subscriptions = subscriptions;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(List<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }

    public Long getId() {
        return id;
    }
    public User getUser() {
        return user;
    }

}
