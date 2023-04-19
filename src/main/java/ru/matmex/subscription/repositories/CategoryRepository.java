package ru.matmex.subscription.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.matmex.subscription.entities.Subscription;

public interface CategoryRepository extends CrudRepository<Subscription, Long> {
}
