package ru.matmex.subscription.services.impl;


import com.google.api.client.auth.oauth2.Credential;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.matmex.subscription.SubscriptionApplication;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.services.CalendarService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.UserService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class GoogleCalendarService implements CalendarService {
    private final SubscriptionService subscriptionService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);

    @Autowired
    public GoogleCalendarService(SubscriptionService subscriptionService, UserService userService) {
        this.subscriptionService = subscriptionService;
        this.userService = userService;
    }

    /**
     * Идентификатор календаря.
     * primary - ключевое слово,
     * позволяет получить доступ к основному календарю текущего пользователя, вошедшего в систему
     */
    private static final String CALENDAR_ID = "primary";
    /**
     * Global instance of the JSON factory.
     */
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Directory to store authorization tokens for this application.
     */
    private static final String TOKENS_DIRECTORY_PATH = "tokens";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES =
            Collections.singletonList(CalendarScopes.CALENDAR);
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";

    /**
     * TODO излишняя логика
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load client secrets.
        InputStream in = GoogleCalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        //returns an authorized Credential object.
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }


    @Override
    public void copySubscriptionsInCalendar() {
        GoogleCredentials credentials =
                GoogleCredentials.newBuilder().setAccessToken(new AccessToken("token", null)).build();
        insertSubscriptionInCalendar(getCalendar(), subscriptionService.getSubscriptions());

    }

    private void insertSubscriptionInCalendar(Calendar calendar, List<SubscriptionModel> subscriptions) {
        for (SubscriptionModel subscription : subscriptions) {
            Event event = new Event();

            event.setSummary(subscription.name());
            DateTime date = DateTime.parseRfc3339(subscription.paymentDate().toString());
            event.setStart(new EventDateTime().setDate(date));
            event.setEnd(new EventDateTime().setDate(date));
            event.setRecurrence(List.of("RRULE:FREQ=MONTH;UNTIL=20110701T170000Z"));
            try {
                calendar.events().insert(CALENDAR_ID, event).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private Calendar getCalendar() {
        try {
            NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

            return new Calendar.Builder(HTTP_TRANSPORT,
                    JSON_FACTORY,
                    getCredentials(HTTP_TRANSPORT))
                    .setApplicationName(SubscriptionApplication.class.getName())
                    .build();

        } catch (GeneralSecurityException | IOException e) {
            logger.error(
                    String.format("Не удалось получить гугл-календарь пользователь - %s,  причина - %s",
                            userService.getCurrentUser(), e.getMessage()));
            throw new RuntimeException(
                    String.format("Не удалось получить гугл-календарь пользователя: %s",
                            userService.getCurrentUser()));

        }
    }
}
