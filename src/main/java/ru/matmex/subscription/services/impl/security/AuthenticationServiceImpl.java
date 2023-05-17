package ru.matmex.subscription.services.impl.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.matmex.subscription.models.security.AuthRequest;
import ru.matmex.subscription.models.security.AuthResponse;
import ru.matmex.subscription.services.AuthenticationService;
import ru.matmex.subscription.services.utils.JwtUtils;

/**
 * Реализация сервиса для аутентификации пользователей
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Авторизация пользователя
     *
     * @param authRequest авторазационные данные пользователя
     * @return ответ, содержащий необходимую авторизационную информацию
     */
    @Override
    public AuthResponse authUser(AuthRequest authRequest) {
        checkOnNull(authRequest);
        Authentication authentication = getAuthentication(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return new AuthResponse(userDetails.getUsername(), jwt);
    }

    /**
     * Проверить что авторизационные аргументы не null
     *
     * @param authRequest авторазационные данные пользователя
     */
    private void checkOnNull(AuthRequest authRequest) {
        if (authRequest.username() == null || authRequest.username().equals("") || authRequest.password() == null || authRequest.password().equals("")) {
            throw new IllegalArgumentException("Аргументы для входа не могут быть пустыми!");
        }
    }

    /**
     * Получить аутенфикацию пользователя
     *
     * @param authRequest авторазационные данные пользователя
     * @return пользователя (Principal) с точки зрения Spring Security.
     */
    private Authentication getAuthentication(AuthRequest authRequest) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()));
    }
}
