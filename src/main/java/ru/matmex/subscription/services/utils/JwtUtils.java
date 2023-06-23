package ru.matmex.subscription.services.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

/**
 * Инструменты для работы с JWT
 */
@Component
public class JwtUtils {
    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;
    private final SecretKey key;

    /**
     * Преобразование секретного ключа
     *
     * @param secretKey "сырой" секретный ключ
     */
    public JwtUtils(@Value("${jwt.secretkey}") String secretKey) {
        key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * генерация JWT
     *
     * @param authentication пользователь (Principal) с точки зрения Spring Security.
     * @return JWT
     */
    public String generateJwtToken(Authentication authentication) {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userDetails.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    /**
     * Проверка JWT
     *
     * @param jwt
     * @return является ли токен валидным
     */
    public boolean validateJwtToken(String jwt) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt);
            return true;
        } catch (MalformedJwtException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * Получить имя пользователя из JWT токена
     *
     * @param jwt
     * @return имя пользователя
     */
    public String getUserNameFromJwtToken(String jwt) {
        return Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody()
                .getSubject();
    }
}
