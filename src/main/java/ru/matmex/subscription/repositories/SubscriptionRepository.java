package ru.matmex.subscription.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.matmex.subscription.entities.Subscription;

/**
 * Репозиторий для работы с сущностями подписки
 */
public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
}
