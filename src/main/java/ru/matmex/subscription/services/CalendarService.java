package ru.matmex.subscription.services;

/**
 * Сервис для работы со сторонними календарями
 */
public interface CalendarService {
    /**
     * Копирование всех подписок и их сроков действия в календарь
     */
    void copySubscriptionsInCalendar();
}
