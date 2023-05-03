package ru.matmex.subscription.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.matmex.subscription.entities.Category;
import ru.matmex.subscription.entities.Subscription;
import ru.matmex.subscription.entities.User;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findCategoryByName(String name);

    Optional<List<Category>> findCategoriesByUser(User user);

    Optional<Category> findCategoryById(Long id);
}
