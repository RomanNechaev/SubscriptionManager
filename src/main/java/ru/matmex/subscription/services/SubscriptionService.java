package ru.matmex.subscription.services;

import jakarta.persistence.EntityNotFoundException;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.models.subscription.CreateSubscriptionModel;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.models.subscription.UpdateSubscriptionModel;

import java.util.List;

public interface SubscriptionService {

    /**
     * Получить спиоск всех подписок текущего пользователя
     *
     * @return список всех подписок текущего пользователя
     */
    List<SubscriptionModel> getSubscriptions();

    /**
     * Получить подписку
     *
     * @param name имя подписки
     * @return модель подписки
     */
    SubscriptionModel getSubscription(String name);

    /**
     * Получить список подписок определенного пользователя
     *
     * @param user - сущность пользователя
     * @return список подписок
     */
    List<Subscription> getSubscriptionsByUser(User user);

    /**
     * Создать подписку
     *
     * @param createSubscriptionModel данные, заполненные пользователем при создании подписки на клиенте
     * @return модель подписки
     */
    SubscriptionModel createSubscription(CreateSubscriptionModel createSubscriptionModel);

    /**
     * Удалить подписку
     *
     * @param id - индефикатор подписки в БД
     * @return сообщение об успешном удалении
     * @throws EntityNotFoundException - ошибка о том, что подписка не найдена
     */
    String deleteSubscription(Long id) throws EntityNotFoundException;

    /**
     * Обновить подписку
     *
     * @param updateSubscriptionModel - параметры, заполненные пользователем на клиенте
     * @return модель подписки
     */
    SubscriptionModel updateSubscription(UpdateSubscriptionModel updateSubscriptionModel);
}
