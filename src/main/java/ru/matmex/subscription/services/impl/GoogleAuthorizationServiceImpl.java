package ru.matmex.subscription.services.impl;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.models.security.GoogleCredentialHandler;
import ru.matmex.subscription.services.GoogleAuthorizationService;
import ru.matmex.subscription.services.UserService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

/**
 * Сервис для работы с авторизаций в гугл аккаунт
 */

@Service
public class GoogleAuthorizationServiceImpl implements GoogleAuthorizationService {
    private final UserService userService;

    /**
     * Глобальный экземпляр фабрики JSON.
     */
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * Глобальный экземпляр областей, необходимых для  API  гугл календаря
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    /**
     * Учетные данные от приложения
     */
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    private final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    /**
     * Управление процессом авторизации
     */
    private GoogleAuthorizationCodeFlow flow;
    /**
     * Локальный сервер, использующийся для процесса авторизации по протоколу OAuth 2.0.
     */
    private LocalServerReceiver receiver;

    public GoogleAuthorizationServiceImpl(UserService userService) throws GeneralSecurityException, IOException {
        this.userService = userService;
        init();
    }
    @Override
    public Credential getCredentials(String responseUrl) throws IOException {
        return new GoogleCredentialHandler(flow, receiver, userService).authorize(responseUrl);
    }
    @Override
    public String getAuthorizationUrl() throws IOException {
        String redirectUri = receiver.getRedirectUri();
        AuthorizationCodeRequestUrl authorizationUrl = flow.newAuthorizationUrl().setRedirectUri(redirectUri);
        return authorizationUrl.build();
    }

    @Override
    public Credential getCurrentUserCredential() throws IOException {
        return new GoogleCredentialHandler(flow,receiver,userService).loadCredential(userService.getGoogleCredential());
    }

    private void init() throws IOException {
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setAccessType("offline")
                .build();
        receiver = new LocalServerReceiver.Builder().setPort(8888).build();
    }

    public NetHttpTransport getHttpTransport() {
        return HTTP_TRANSPORT;
    }
}
