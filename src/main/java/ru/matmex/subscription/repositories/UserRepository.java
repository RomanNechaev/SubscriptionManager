package ru.matmex.subscription.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.lang.NonNull;
import ru.matmex.subscription.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> getById(Long id);

    Boolean existsByUsername(String username);

    List<User> findAll();

}
