package ru.matmex.subscription.controllers;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.calendar.Calendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.matmex.subscription.services.CalendarService;



/**
 * Контроллер для работы с гугл-сервисами: гугл-календарь и гугл-диск
 */
@Controller
public class GoogleServicesController {
    @Autowired
    private CalendarService googleCalendarService;
    @Autowired
    public GoogleServicesController(CalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    /**
     * Перенос подпискок пользователя и их сроков действия в гугл-календарь
     */
    @RequestMapping(value = "/api/app/google/calendar")
    public ResponseEntity<String> copySubscriptionsInCalendar(@AuthenticationPrincipal OAuth2User oAuth2User) {
        com.google.api.services.calendar.model.Events eventList;
        String message;
        try {
            CustomOAuth2User customOAuth2User = (CustomOAuth2User)oAuth2User;
            String token = customOAuth2User.getToken();
            GoogleCredential credential = new GoogleCredential().setAccessToken(token);


            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            Calendar service = new Calendar.Builder(httpTransport, JSON_FACTORY, credential)
                    .setApplicationName(APPLICATION_NAME).build();
            Events events = service.events();
            service.
            eventList = events.list("primary").setTimeZone("Asia/Kolkata").setTimeMin(date1).setTimeMax(date2).setQ(q).execute();
            message = eventList.getItems().toString();
            System.out.println("My:" + eventList.getItems());
        } catch (Exception e) {

            message = "Exception while handling OAuth2 callback (" + e.getMessage() + ")."
                    + " Redirecting to google connection status page.";
        }
        return new ResponseEntity<>("", HttpStatus.OK);
    }
}
