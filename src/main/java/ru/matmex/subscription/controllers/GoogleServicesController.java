package ru.matmex.subscription.controllers;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.matmex.subscription.services.CalendarService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;


/**
 * Контроллер для работы с гугл-сервисами: гугл-календарь и гугл-диск
 */
@Controller
public class GoogleServicesController {
    @Autowired
    private final CalendarService googleCalendarService;
    @Autowired
    public GoogleServicesController(CalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    /**
     * Перенос подпискок пользователя и их сроков действия в гугл-календарь
     */
    @RequestMapping(value = "/api/app/google/calendar")
    public ResponseEntity<String> copySubscriptionsInGoogleCalendar(@AuthenticationPrincipal OAuth2User oAuth2User) {
        googleCalendarService.copySubscriptionsInCalendar();
        return new ResponseEntity<>("Перенос подпискок успешно совершен", HttpStatus.OK);
    }

}
