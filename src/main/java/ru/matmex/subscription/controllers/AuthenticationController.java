package ru.matmex.subscription.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.matmex.subscription.models.security.AuthRequest;
import ru.matmex.subscription.models.security.AuthResponse;
import ru.matmex.subscription.services.AuthenticationService;

@Controller
@CrossOrigin
public class AuthenticationController {
    private final AuthenticationService authenticationService;
    @Autowired
    public AuthenticationController(AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }
    @PostMapping("auth/login")
    public ResponseEntity<AuthResponse> authUser(@RequestBody AuthRequest authRequest) {
        return ResponseEntity.ok(authenticationService.authUser(authRequest));
    }
}
