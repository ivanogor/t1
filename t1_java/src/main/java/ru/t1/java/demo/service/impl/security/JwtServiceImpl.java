package ru.t1.java.demo.service.impl.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import ru.t1.java.demo.model.User;
import ru.t1.java.demo.service.security.JwtService;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Реализация сервиса для работы с JWT-токенами.
 * Предоставляет методы для генерации, валидации и извлечения данных из токенов.
 *
 * @author ivanogor
 * @version 1.0
 * @since 21.11.2024
 */
@Service
public class JwtServiceImpl implements JwtService {
    @Value("${security.token}")
    private String jwtSigningKey;

    @Value("${security.expiration}")
    private Long expirationTime;

    /**
     * Извлекает имя пользователя из JWT-токена.
     *
     * @param token JWT-токен.
     * @return Имя пользователя.
     */
    @Override
    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Генерирует JWT-токен для заданных данных пользователя.
     *
     * @param userDetails Данные пользователя.
     * @return Сгенерированный JWT-токен.
     */
    @Override
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        if (userDetails instanceof User customUserDetails) {
            claims.put("id", customUserDetails.getId());
            claims.put("email", customUserDetails.getEmail());
            claims.put("username", customUserDetails.getUsername());
            claims.put("role", customUserDetails.getRole());
        }
        return generateToken(claims, userDetails);
    }

    /**
     * Проверяет валидность JWT-токена.
     *
     * @param token       JWT-токен.
     * @param userDetails Данные пользователя.
     * @return true, если токен валиден, иначе false.
     */
    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Извлекает данные из JWT-токена.
     *
     * @param token           JWT-токен.
     * @param claimsResolvers Функция для извлечения данных.
     * @param <T>             Тип данных.
     * @return Извлеченные данные.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolvers) {
        final Claims claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    /**
     * Генерирует JWT-токен с дополнительными данными.
     *
     * @param extraClaims Дополнительные данные.
     * @param userDetails Данные пользователя.
     * @return Сгенерированный JWT-токен.
     */
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256).compact();
    }

    /**
     * Проверяет, истек ли срок действия JWT-токена.
     *
     * @param token JWT-токен.
     * @return true, если токен истек, иначе false.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Извлекает дату истечения срока действия JWT-токена.
     *
     * @param token JWT-токен.
     * @return Дата истечения срока действия.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Извлекает все данные из JWT-токена.
     *
     * @param token JWT-токен.
     * @return Все данные из токена.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Получает ключ для подписи JWT-токена.
     *
     * @return Ключ для подписи.
     */
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}