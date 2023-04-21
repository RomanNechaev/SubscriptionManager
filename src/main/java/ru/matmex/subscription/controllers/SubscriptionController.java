package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;

import java.util.List;

@Controller
public class SubscriptionController {
    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    SubscriptionController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "/subscription/username")
    public ResponseEntity<List<Subscription>> getSubscriptionByUsername(String username) {
        User user = userService.loadByUsername(username);
        return ResponseEntity.ok(subscriptionService.getSubscriptions(user));
    }

}
