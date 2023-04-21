package ru.matmex.subscription.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends CrudRepository<Subscription, Long> {
    Optional<Subscription> findSubscriptionById(Long id);
    Optional<List<Subscription>> findSubscriptionByUser(User user);
}
