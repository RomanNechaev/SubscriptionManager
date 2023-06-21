package ru.matmex.subscription.services.impl;


import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.SubscriptionApplication;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.services.CalendarService;
import ru.matmex.subscription.services.GoogleAuthorizationService;
import ru.matmex.subscription.services.SubscriptionService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;

@Service
public class GoogleCalendarService implements CalendarService {
    private final SubscriptionService subscriptionService;
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);
    private final GoogleAuthorizationService googleAuthorizationService;

    @Autowired
    public GoogleCalendarService(SubscriptionService subscriptionService, GoogleAuthorizationService googleAuthorizationService) {
        this.subscriptionService = subscriptionService;
        this.googleAuthorizationService = googleAuthorizationService;
    }

    /**
     * Идентификатор календаря.
     * primary - ключевое слово,
     * позволяет получить доступ к основному календарю текущего пользователя, вошедшего в систему
     */
    private static final String CALENDAR_ID = "primary";

    @Override
    public void copySubscriptionsInCalendar() {
        try {
            insertSubscriptionInCalendar(getCalendar(), subscriptionService.getSubscriptions());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void insertSubscriptionInCalendar(Calendar calendar, List<SubscriptionModel> subscriptions) throws IOException {
        List<Event> events = calendar.events().list(CALENDAR_ID).execute().getItems();
        for (SubscriptionModel subscription : subscriptions) {
            if (events.stream().noneMatch(event -> Objects.equals(event.getSummary(), subscription.name()))) {
                Event event = new Event();
                event.setSummary(subscription.name());
                Date startDate = new Date(subscription.paymentDate().getTime() + 123);
                Date endDate = new Date(startDate.getTime() + 86400000);
                DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
                DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
                event.setStart(new EventDateTime().setDateTime(start));
                event.setEnd(new EventDateTime().setDateTime(end));
                try {
                    calendar.events().insert(CALENDAR_ID, event).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    private Calendar getCalendar() throws SocketTimeoutException {
        try {
            return new Calendar.Builder(googleAuthorizationService.getHttpTransport(),
                    JSON_FACTORY,
                    googleAuthorizationService.getCurrentUserCredential())
                    .setApplicationName(SubscriptionApplication.class.getName())
                    .build();

        } catch (SocketTimeoutException e) {
            logger.error("Время подключения истекло");
            throw new SocketTimeoutException("Время подключения вышло! Попробуй еще раз!");
        } catch (IOException e) {
            logger.error("Не удалось получить гугл-календарь пользователя");
            throw new RuntimeException("Не удалось получить гугл-календарь пользователя");
        }
    }
}