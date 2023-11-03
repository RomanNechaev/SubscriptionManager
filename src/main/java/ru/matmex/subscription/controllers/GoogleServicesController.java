package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.matmex.subscription.services.CalendarService;

/**
 * Контроллер для работы с гугл-сервисами: гугл-календарь и гугл-диск
 */
@Controller
public class GoogleServicesController {
    private final CalendarService googleCalendarService;

    @Autowired
    public GoogleServicesController(CalendarService googleCalendarService) {
        this.googleCalendarService = googleCalendarService;
    }

    /**
     * Перенос подпискок пользователя и их сроков действия в гугл-календарь
     */
    @RequestMapping(value = "/api/app/google/calendar")
    public ResponseEntity<String> copySubscriptionsInGoogleCalendar() {
        googleCalendarService.copySubscriptionsInCalendar();
        return new ResponseEntity<>("Перенос подпискок успешно совершен", HttpStatus.OK);
    }

}
