package ru.matmex.subscription.services.notifications.telegram;

import ru.matmex.subscription.entities.User;

/**
 * Обрабатывает сообщения, поступаемые от пользователя
 */
public class CommandHandler {
    private final TelegramBot bot;

    public CommandHandler(TelegramBot bot) {
        this.bot = bot;
    }

    /**
     * Обработка сообщений от пользователя
     *
     * @param user        - текущий пользователь в системе
     * @param messageText - сообщения от пользователя
     * @param chatId      - id чата в тг
     * @return получилось ли привязать бота
     */
    public boolean processCommand(User user, String messageText, Long chatId) {
        String currentUsername = user.getUsername();
        if (messageText.equals(currentUsername) && user.getTelegramChatId() == null) {
            bot.sendMessage(chatId, "Уведомление через бота успешно привязано!");
            return true;
        } else {
            if (user.getTelegramChatId() != null) {
                bot.sendMessage(chatId, "Вы уже привязали бота!");
                return false;
            }
            if (messageText.equals("/start")) {
                bot.sendMessage(chatId, "Введите имя пользователя");
                return false;
            }
            bot.sendMessage(chatId, "Попробуй еще раз");
            return false;
        }
    }
}
