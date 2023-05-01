package ru.matmex.subscription.services;

import ru.matmex.subscription.models.security.AuthRequest;
import ru.matmex.subscription.models.security.AuthResponse;

public interface AuthenticationService {
    AuthResponse authUser(AuthRequest authRequest);
}
