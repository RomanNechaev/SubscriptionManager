package ru.matmex.subscription.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.matmex.subscription.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> getById(Long id);

    Optional<Void> deleteUserByUsername(String username);

    Boolean existsByUsername(String username);

    @Query("SELECT user FROM users WHERE users.")
    Optional<List<User>> getUsersByUserRole();

}
