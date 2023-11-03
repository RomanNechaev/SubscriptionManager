package ru.matmex.subscription.services.impl;


import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.SubscriptionApplication;
import ru.matmex.subscription.models.subscription.SubscriptionModel;
import ru.matmex.subscription.services.CalendarService;
import ru.matmex.subscription.services.GoogleAuthorizationService;
import ru.matmex.subscription.services.SubscriptionService;
import ru.matmex.subscription.services.utils.GoogleUtils;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.Objects;

@Service
public class GoogleCalendarService implements CalendarService {
    private final SubscriptionService subscriptionService;
    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final Logger logger = LoggerFactory.getLogger(GoogleCalendarService.class);
    private final GoogleAuthorizationService googleAuthorizationService;
    private final GoogleUtils googleUtils = new GoogleUtils();

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
            insertSubscriptionsInCalendar(getCalendar(), subscriptionService.getSubscriptions());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Вставка в гугл-календарь подписок пользователя,
     * если какая-то подписка уже имеется в календаря, то она не вставляется.
     */
    private void insertSubscriptionsInCalendar(Calendar calendar, List<SubscriptionModel> subscriptions) throws IOException {
        List<Event> events = calendar.events().list(CALENDAR_ID).execute().getItems();
        for (SubscriptionModel subscription : subscriptions) {
            if (events.stream().noneMatch(event -> Objects.equals(event.getSummary(), subscription.name()))) {
                Event newEvent = googleUtils.subscriptionFormationAsEvents(subscription);
                try {
                    calendar.events().insert(CALENDAR_ID, newEvent).execute();
                } catch (IOException e) {
                    logger.error("Не удалось вставить подписку подписку в календарь пользователя");
                    throw new IOException("Не получилось вставить подписку в календарь");
                }
            }
        }
    }

    /**
     * Получение гугл-календаря пользователя
     */
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