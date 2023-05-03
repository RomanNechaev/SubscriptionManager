package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;
import ru.matmex.subscription.services.SubscriptionService;
import java.util.List;

@Controller
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping(value = "/api/app/subscriptions")
    public ResponseEntity<List<SubscriptionModel>> getSubscriptionsByCurrentUsername() {
        return ResponseEntity.ok(subscriptionService.getSubscriptions());
    }

    @GetMapping(value = "/api/app/subscription/{name}")
    public ResponseEntity<SubscriptionModel> getSubscriptionByName(@PathVariable String name) {
        return ResponseEntity.ok(subscriptionService.getSubscription(name));
    }

    @PostMapping(value = "/api/app/subscription")
    public ResponseEntity<SubscriptionModel> create(@RequestBody CreateSubscriptionModel createSubscriptionModel) {
        return ResponseEntity.ok(subscriptionService.createSubscription(createSubscriptionModel));
    }

    @PutMapping(value = "/api/app/subscription")
    public ResponseEntity<SubscriptionModel> update(@RequestBody UpdateSubscriptionModel updateSubscriptionModel) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(updateSubscriptionModel));
    }

    @DeleteMapping(value = "/api/app/subscription/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        subscriptionService.deleteSubscription(id);
        return ResponseEntity.ok(subscriptionService.deleteSubscription(id));
    }
}
