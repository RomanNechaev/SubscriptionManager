package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.security.GoogleAuthLink;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.services.GoogleAuthorizationService;
import ru.matmex.subscription.services.UserService;
import ru.matmex.subscription.services.impl.GoogleAuthorizationServiceImpl;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Контроллер для операций с пользователем
 */
@Controller
public class UserController {
    private final UserService userService;

    private final GoogleAuthorizationService googleAuthorizationService;

    @Autowired
    public UserController(UserService userService) throws GeneralSecurityException, IOException {
        this.userService = userService;
        this.googleAuthorizationService = new GoogleAuthorizationServiceImpl(userService);
    }

    /**
     * Обновить пользовательские данные
     *
     * @param userUpdateModel - данные для обноваления пользвателя
     * @return HTTP ответ с информацией об обноленном пользователи
     */
    @PostMapping(value = "/api/app/user")
    public ResponseEntity<UserModel> update(@RequestBody UserUpdateModel userUpdateModel) {
        return ResponseEntity.ok(userService.updateUser(userUpdateModel));
    }

    /**
     * Получить информацию о всех пользователей
     *
     * @return HTTP ответ с информацией о всех пользователях
     */
    @GetMapping(value = "/api/admin/app/users")
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    /**
     * Получить информацию о пользователе по имени
     *
     * @param username имя пользователя
     * @return HTTP ответ с информаицей о пользователе
     */
    @GetMapping(value = "/api/admin/app/{username}")
    public ResponseEntity<UserModel> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUser(username));
    }

    /**
     * Узнаить информацю о подключении к гугл аккаунту
     *
     * @return HTTP ответ с информаицей
     */
    @GetMapping(value = "/api/app/google")
    public ResponseEntity<String> IsLinkedGoogleAccount() {
        return ResponseEntity.ok(userService.getInformationAboutGoogle());
    }

    /**
     * Получить сслыку для авторизации гугл аккаунта
     *
     * @return сслыка для авторизации гугл аккаунта
     * @throws IOException
     */
    @GetMapping(value = "/api/app/google/link")
    public ResponseEntity<String> linkGoogleAccount() throws IOException {
        return ResponseEntity.ok(googleAuthorizationService.getAuthorizationUrl());
    }

    /**
     * Авторизовать гугл аккаунт
     *
     * @param request ссылка в браузере после авторизации в гугл аккаунт
     * @return информация об авторизации
     * @throws IOException
     */
    @PostMapping("/api/app/google/authorize")
    public ResponseEntity<String> authorizeToGoogleAccount(@RequestBody GoogleAuthLink request) throws IOException {
        googleAuthorizationService.getCredentials(request.authorizationLink());
        return ResponseEntity.ok("Все ок!");
    }

    /**
     * Удалить пользователя
     *
     * @param username имя пользователя
     * @return HTTP ответ с информаиией об успешном удалении
     */
    @DeleteMapping(value = "/api/admin/app/{username}")
    public ResponseEntity<String> delete(@PathVariable String username) {
        return ResponseEntity.ok(userService.delete(username));
    }
}
