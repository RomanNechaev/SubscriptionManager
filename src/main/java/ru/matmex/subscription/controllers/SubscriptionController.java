package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.SubscriptionModel;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.utils.MappingUtils;

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

    @GetMapping(value = "/subscriptions/{username}")
    public ResponseEntity<List<SubscriptionModel>> getSubscriptionsByUsername(@PathVariable String username) {
        User user = userService.loadByUsername(username);
        return ResponseEntity.ok(subscriptionService.getSubscriptions(user));
    }

    @GetMapping(value = "/subscription/{username}/{name}")
    public ResponseEntity<SubscriptionModel> getSubscriptionByName(@PathVariable String username, @PathVariable String name) {
        User user = userService.loadByUsername(username);
        return ResponseEntity.ok(subscriptionService.getSubscription(user, name));
    }

    @PostMapping(value = "/subscription")
    public ResponseEntity<SubscriptionModel> create(@RequestBody SubscriptionModel subscriptionModel) {
        return ResponseEntity.ok(MappingUtils.mapToSubscriptionModel(subscriptionService.createSubscription(subscriptionModel)));
    }

    @PutMapping(value = "/subscription")
    public ResponseEntity<SubscriptionModel> update(@RequestBody SubscriptionModel subscriptionModel) {
        return ResponseEntity.ok(MappingUtils.mapToSubscriptionModel(subscriptionService.updateSubscription(subscriptionModel)));
    }

    @DeleteMapping(value = "/subscription{id}")
    public void delete(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
    }
}
