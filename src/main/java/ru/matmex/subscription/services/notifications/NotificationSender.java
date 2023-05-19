package ru.matmex.subscription.services.notifications;

public interface NotificationSender {
    void sendNotification(Notification notification);
    void subscribe();

}
