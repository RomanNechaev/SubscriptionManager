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

/**
 * Контроллер для операций с подписками
 */
@Controller
public class SubscriptionController {
    private final SubscriptionService subscriptionService;

    @Autowired
    SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    /**
     * Получить подписки текущего пользователя
     *
     * @return HTTP ответ с подписками текущего пользователя
     */
    @GetMapping(value = "/api/app/subscriptions")
    public ResponseEntity<List<SubscriptionModel>> getSubscriptionsByCurrentUsername() {
        return ResponseEntity.ok(subscriptionService.getSubscriptions());
    }

    /**
     * Получить информацию о подписке по названию
     *
     * @param name - название подписки
     * @return HTTP ответ с информацией о подписки
     */
    @GetMapping(value = "/api/app/subscription/{name}")
    public ResponseEntity<SubscriptionModel> getSubscriptionByName(@PathVariable String name) {
        return ResponseEntity.ok(subscriptionService.getSubscription(name));
    }

    /**
     * Создать подписку
     *
     * @param createSubscriptionModel данные о подписки, заполненные пользователем на клиенте
     * @return HTTP ответ с информаицей об созданной подписке
     */
    @PostMapping(value = "/api/app/subscription")
    public ResponseEntity<SubscriptionModel> create(@RequestBody CreateSubscriptionModel createSubscriptionModel) {
        return ResponseEntity.ok(subscriptionService.createSubscription(createSubscriptionModel));
    }

    /**
     * Обновить подписку
     *
     * @param updateSubscriptionModel данные об обновленной подписке, заполненные пользователем на клиенте
     * @return HTTP ответ с информаицей об обновленной подписке
     */
    @PutMapping(value = "/api/app/subscription")
    public ResponseEntity<SubscriptionModel> update(@RequestBody UpdateSubscriptionModel updateSubscriptionModel) {
        return ResponseEntity.ok(subscriptionService.updateSubscription(updateSubscriptionModel));
    }

    /**
     * Удалить подписку
     *
     * @param id - индефикатор подписки в БД
     * @return HTTP ответ об успешном удалении подписки
     */
    @DeleteMapping(value = "/api/app/subscription/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        return ResponseEntity.ok(subscriptionService.deleteSubscription(id));
    }
}
