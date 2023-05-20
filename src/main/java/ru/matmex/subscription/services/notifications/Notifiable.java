package ru.matmex.subscription.services.notifications;

public abstract class Notifiable {
    public void registerNotification(String message) {
        NotificationBroker.getInstance().addNotification(new Notification(message));
        NotificationBroker.getInstance().notifyAllSubscriber();
    }

}
