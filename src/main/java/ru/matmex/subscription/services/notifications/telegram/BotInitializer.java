package ru.matmex.subscription.services.notifications.telegram;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.matmex.subscription.services.UserService;

/**
 * Инициализация телеграмм бота
 */
@Component
public class BotInitializer {
    private final BotConfig botConfig;
    private final UserService userService;

    @Autowired
    public BotInitializer(BotConfig botConfig, UserService userService) {
        this.botConfig = botConfig;
        this.userService = userService;
    }

    /**
     * Инициализирует бота при старте или обновлении приложения
     *
     * @throws TelegramApiException
     */
    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBot telegramBot = new TelegramBot(botConfig, userService);
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
