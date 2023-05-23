package ru.matmex.subscription.services.notifications;

/**
 * Позволяет контроллерам регистировать события
 */
public abstract class Notifiable {
    public void registerNotification(String message, String username) {
        NotificationBroker.getInstance().addNotification(new Notification(message, username));
        NotificationBroker.getInstance().notifyAllSubscriber();
    }

    /**
     * Добавить рассыльщика
     */
    public void addNotificationSender(NotificationSender sender) {
        NotificationBroker.getInstance().addNotificationSender(sender);
    }

}
