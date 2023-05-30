package ru.matmex.subscription.services.notifications.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.matmex.subscription.entities.User;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.security.AuthenticationContext;
import ru.matmex.subscription.services.notifications.Notification;
import ru.matmex.subscription.services.notifications.NotificationBroker;
import ru.matmex.subscription.services.notifications.NotificationSender;

public class TelegramBot extends TelegramLongPollingBot implements NotificationSender {
    private final BotConfig botConfig;
    private final UserService userService;

    private final CommandHandler handler = new CommandHandler(this);
    private static final Logger logger = LoggerFactory.getLogger(TelegramBot.class);

    public TelegramBot(BotConfig botConfig, UserService userService) {
        this.botConfig = botConfig;
        this.userService = userService;
    }

    @Override
    public String getBotToken() {
        return botConfig.getToken();
    }

    /**
     * Принимает и обрабатывает обновления состояния бота.
     *
     * @param update - состояние отдельного пользователя
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();
            User currentUser = userService.getUser(AuthenticationContext.getAuthenticationContext().getName());
            boolean isLinked = handler.processCommand(currentUser, messageText, chatId);
            if (isLinked) {
                userService.setTelegramChatId(currentUser.getUsername(), chatId);
                NotificationBroker.getInstance().addNotificationSender(this);
            }
        }
    }

    /**
     * Получить имя тг бота
     *
     * @return имя тг бота
     */
    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    /**
     * Отправить сообщение пользователю
     *
     * @param chatId     id тг чата, куда отправляется сообщение
     * @param textToSend - текст сообщения
     */
    public void sendMessage(Long chatId, String textToSend) {
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
        String message = notification.getMessage() + "\nДата отправки события: " + notification.getCurrentDate();
        Long chatId = userService.getUserModel(notification.getUsername()).tgId();
        sendMessage(chatId, message);
    }

}
