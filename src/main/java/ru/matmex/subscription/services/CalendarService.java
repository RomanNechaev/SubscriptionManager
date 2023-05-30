package ru.matmex.subscription.services;

import java.io.IOException;

/**
 * Сервис для работы со сторонними календарями
 */
public interface CalendarService {
    /**
     * Копирование всех подписок и их сроков действия в календарь
     */
    void copySubscriptionsInCalendar();
}
