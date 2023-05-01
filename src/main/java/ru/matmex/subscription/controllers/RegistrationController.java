package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserRegistrationModel;
import ru.matmex.subscription.services.UserService;

@Controller
@CrossOrigin
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }
    @PostMapping("/registration")
    public ResponseEntity<UserModel> registration(@RequestBody UserRegistrationModel userRegistrationModel) {
        return ResponseEntity.ok(userService.adduser(userRegistrationModel));
    }
}
