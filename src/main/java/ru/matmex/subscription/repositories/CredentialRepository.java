package ru.matmex.subscription.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.matmex.subscription.entities.GoogleCredential;

/**
 * Репозитория для работы с учетными данными от гугл аккаунта
 */
public interface CredentialRepository extends JpaRepository<GoogleCredential,Long> {
}
