package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.notifications.Notifiable;
import ru.matmex.subscription.services.notifications.email.EmailNotificationSender;

/**
 * Контроллер для регистрации пользователя
 */
@Controller
@CrossOrigin
public class RegistrationController extends Notifiable {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService, EmailNotificationSender sender) {
        this.userService = userService;
        addNotificationSender(sender);
    }

    /**
     * Регистрация пользователя в приложении
     *
     * @param userRegistrationModel - данные о пользователе
     * @return HTTP ответ с данными о пользователе
     */
    @PostMapping("/registration")
    public ResponseEntity<UserModel> registration(@RequestBody UserRegistrationModel userRegistrationModel) {
        UserModel userModel = userService.adduser(userRegistrationModel);
        registerNotification("Вы успешно зарегистрировались в приложении!", userRegistrationModel.username());
        return ResponseEntity.ok(userModel);
    }
}
