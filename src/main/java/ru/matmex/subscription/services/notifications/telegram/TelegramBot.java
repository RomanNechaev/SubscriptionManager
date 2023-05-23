package ru.matmex.subscription.services.notifications.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.notifications.Notification;
import ru.matmex.subscription.services.notifications.NotificationSender;

@Service
public class TelegramBot extends TelegramLongPollingBot implements NotificationSender {
    private final BotConfig botConfig;
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);
    private final UserService userService;
    private long chatId;
    private String currentUserName;

    @Autowired
    public TelegramBot(BotConfig botConfig, UserService userService) {
        this.botConfig = botConfig;
        this.userService = userService;
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            chatId = update.getMessage().getChatId();
            processCommand(messageText);
        }
    }

    /**
     * Обработка сообщений от пользователя
     *
     * @param messageText - пользовательское сообщение
     */
    private void processCommand(String messageText) {
        if (messageText.equals("secret")) {
            sendMessage(chatId, "Уведомление через бота успешно привязано!");
            userService.setTelegramChatId(currentUserName, chatId);
        } else {
            switch (messageText) {
                case "/start" -> sendMessage(chatId, "Введите имя пользователя");
                default -> {
                    currentUserName = messageText;
                    sendMessage(chatId, "Введите код потверждения, отправленный на почту!");
                }
            }
        }
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    private void sendMessage(Long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Не удалось отравить уведомление :(");
        }
    }

    @Override
    public void sendNotification(Notification notification) {
        String message = notification.getMessage() + "  Дата отправки события  " + notification.getCurrentDate();
        sendMessage(chatId, message);
    }

}
