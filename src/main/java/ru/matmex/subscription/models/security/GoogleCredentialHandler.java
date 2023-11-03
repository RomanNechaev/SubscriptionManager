package ru.matmex.subscription.models.security;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.AuthorizationCodeResponseUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.VerificationCodeReceiver;
import com.google.api.client.util.Preconditions;
import ru.matmex.subscription.entities.GoogleCredential;
import ru.matmex.subscription.services.UserService;

import java.io.IOException;

/**
 * Обработчик учетных данных от гугл аккаунта
 */
public class GoogleCredentialHandler {
    private final AuthorizationCodeFlow flow;
    private final VerificationCodeReceiver receiver;
    private final UserService userService;

    public GoogleCredentialHandler(AuthorizationCodeFlow flow, VerificationCodeReceiver receiver, UserService userService) {
        this.flow = Preconditions.checkNotNull(flow);
        this.receiver = Preconditions.checkNotNull(receiver);
        this.userService = userService;
    }

    /**
     * Авторизовать учетные данные от гугл аккаунта
     *
     * @param responseUrl - ссылка, после авторизации гугл аккаунта в браузере
     * @return авторизованные учетные данные
     * @throws IOException
     */
    public Credential authorize(String responseUrl) throws IOException {
        Credential newCredential;
        try {
            GoogleCredential credential = userService.getGoogleCredential();
            if (credential != null && (credential.getRefreshToken() != null)) {
                return loadCredential(credential);
            }
            String redirectUri = "http://localhost:8888/Callback";
            AuthorizationCodeResponseUrl codeResponseUrl = new AuthorizationCodeResponseUrl(responseUrl);
            TokenResponse response = flow.newTokenRequest(codeResponseUrl.getCode()).setRedirectUri(redirectUri).execute();
            newCredential = newCredential();
            newCredential.setFromTokenResponse(response);
            userService.setGoogleCredential(newCredential);
        } finally {
            receiver.stop();
        }

        return newCredential;
    }

    /**
     * Преобразовать учетные данные к валидному состоянию
     *
     * @param credential - учетные данные из БД
     * @return валидные учетные данные пользователя
     */
    public Credential loadCredential(GoogleCredential credential) {
        Credential validCredential = newCredential();
        validCredential.setAccessToken(credential.getAccessToken());
        validCredential.setRefreshToken(credential.getRefreshToken());
        validCredential.setExpirationTimeMilliseconds(credential.getExpirationTimeMilliseconds());
        return validCredential;
    }

    /**
     * Создать новые учетные данные
     *
     * @return учетные данные пользователя
     */
    private Credential newCredential() {
        Credential.Builder builder = (new Credential.Builder(flow.getMethod()))
                .setTransport(flow.getTransport())
                .setJsonFactory(flow.getJsonFactory())
                .setTokenServerEncodedUrl(flow.getTokenServerEncodedUrl())
                .setClientAuthentication(flow.getClientAuthentication())
                .setRequestInitializer(flow.getRequestInitializer())
                .setClock(flow.getClock());
        return builder.build();
    }
}

