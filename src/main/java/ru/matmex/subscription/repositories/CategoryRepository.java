package ru.matmex.subscription.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findCategoryByNameAndUser(String name, User user);

    Optional<List<Category>> findCategoriesByUser(User user);

    Optional<Category> findCategoryById(Long id);

    boolean existsByNameAndUser(String name, User user);
}
