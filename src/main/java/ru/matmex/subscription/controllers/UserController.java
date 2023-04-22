package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.services.UserService;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/user")
    public ResponseEntity<UserModel> update(@RequestBody UserUpdateModel userUpdateModel) {
        return ResponseEntity.ok(userService.updateUsername(userUpdateModel));
    }
}
