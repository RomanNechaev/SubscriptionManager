package ru.matmex.subscription.services;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.http.javanet.NetHttpTransport;

import java.io.IOException;

public interface GoogleAuthorizationService {
    /**
     * @param responseUrl - ссылка, после авторизации гугл аккаунта в браузере
     * @return авторизованные учетные данные клиента
     * @throws IOException если учетные данные не были найдены
     */
    Credential getCredentials(String responseUrl) throws IOException;

    /**
     * @return протокол передачи данных(HTTP|HTTPS)
     */
    NetHttpTransport getHttpTransport();

    /**
     * @return url для авторизации в гугл аккаунт
     * @throws IOException
     */
    String getAuthorizationUrl() throws IOException;

    Credential getCurrentUserCredential() throws IOException;

}
