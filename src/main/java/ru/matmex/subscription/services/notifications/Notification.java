package ru.matmex.subscription.services.notifications;

import java.util.Date;

/**
 * Уведомление, полученное от различных сервисов.
 */
public class Notification {

    private final String message;
    private final Date currentDate;

    public Notification(String message) {
        this.message = message;
        this.currentDate = new Date();
    }

    public String getMessage() {
        return message;
    }

    public Date getCurrentDate() {
        return currentDate;
    }
}
