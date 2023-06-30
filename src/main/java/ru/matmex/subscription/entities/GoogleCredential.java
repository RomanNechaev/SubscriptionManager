package ru.matmex.subscription.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.io.Serializable;

/**
 * Сущность учетных данных от гугл аккаунта
 */
@Entity
public class GoogleCredential implements Serializable {
    private String accessToken;
    private Long expirationTimeMilliseconds;
    private String refreshToken;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public GoogleCredential() {

    }

    public GoogleCredential(String accessToken, Long expirationTimeMilliseconds, String refreshToken) {
        this.accessToken = accessToken;
        this.expirationTimeMilliseconds = expirationTimeMilliseconds;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpirationTimeMilliseconds() {
        return expirationTimeMilliseconds;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
