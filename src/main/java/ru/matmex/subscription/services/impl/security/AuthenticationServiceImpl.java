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
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public AuthenticationServiceImpl(AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public AuthResponse authUser(AuthRequest authRequest) {
        checkOnNull(authRequest);
        Authentication authentication = getAuthentication(authRequest);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        //TODO добавить роли
        return new AuthResponse(userDetails.getUsername(), jwt);
    }

    private void checkOnNull(AuthRequest authRequest) {
        if (authRequest.username() == null || authRequest.username().equals("") || authRequest.password() == null || authRequest.password().equals("")) {
            throw new IllegalArgumentException("Аргументы для входа не могут быть пустыми!");
        }
    }

    private Authentication getAuthentication(AuthRequest authRequest) {
        return authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(
                        authRequest.username(),
                        authRequest.password()));
    }
}
