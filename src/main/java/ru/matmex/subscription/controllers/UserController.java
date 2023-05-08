package ru.matmex.subscription.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.user.UserModel;
import ru.matmex.subscription.models.user.UserUpdateModel;
import ru.matmex.subscription.services.UserService;

import java.util.List;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/api/app/user")
    public ResponseEntity<UserModel> update(@RequestBody UserUpdateModel userUpdateModel) {
        return ResponseEntity.ok(userService.updateUser(userUpdateModel));
    }

    @GetMapping(value = "/api/admin/app/users")
    public ResponseEntity<List<UserModel>> getUsers() {
        return ResponseEntity.ok(userService.getUsers());
    }

    @GetMapping(value = "/api/admin/app/{username}")
    public ResponseEntity<UserModel> getUserByUsername(@PathVariable String username) {
        var t = userService.getUser(username);
        return ResponseEntity.ok(t);
    }

    @DeleteMapping(value = "/api/admin/app/{username}")
    public ResponseEntity<String> delete(@PathVariable String username) {
        return ResponseEntity.ok(userService.delete(username));
    }
}
