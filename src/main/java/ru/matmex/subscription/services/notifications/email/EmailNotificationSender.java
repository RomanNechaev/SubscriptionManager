package ru.matmex.subscription.services.notifications.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.notifications.Notification;
import ru.matmex.subscription.services.notifications.NotificationSender;

@Service
public class EmailNotificationSender implements NotificationSender {
    private final JavaMailSender mailSender;
    private final SimpleMailMessage templateMessage;

    private static final Logger logger = LoggerFactory.getLogger(EmailNotificationSender.class);

    private final UserService userService;

    @Autowired
    public EmailNotificationSender(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
        this.templateMessage = new SimpleMailMessage();
    }


    @Override
    public void sendNotification(Notification notification) {
        SimpleMailMessage message = new SimpleMailMessage(this.templateMessage);
        String userEmail = userService.getCurrentUser().getEmail();
        message.setTo(userEmail);
        message.setText(notification.getMessage());
        message.setSentDate(notification.getCurrentDate());
        try {
            this.mailSender.send(message);
        } catch (MailException exception) {
            logger.error(String.format("Не удалось отправить сообщение на email:%s", userEmail));
        }
    }

    @Override
    public void subscribe() {

    }
}
