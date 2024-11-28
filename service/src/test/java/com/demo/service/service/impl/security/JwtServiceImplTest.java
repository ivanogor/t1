package com.demo.service.service.impl.security;

import com.demo.service.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.demo.service.model.enums.Role.ROLE_USER;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    private String jwtSigningKey;
    private Long expirationTime;
    private UserDetails userDetails;

    @BeforeEach
    public void setUp() {
        jwtSigningKey = "53A73E5F1C4E0A2D3B5F2D784E6A1B423D6F247D1F6E5C3A596D635A75327855"; // Валидный Base64 ключ
        expirationTime = 3600000L; // 1 hour

        ReflectionTestUtils.setField(jwtService, "jwtSigningKey", jwtSigningKey);
        ReflectionTestUtils.setField(jwtService, "expirationTime", expirationTime);

        userDetails = new User();
        ((User) userDetails).setId(1L);
        ((User) userDetails).setEmail("test@example.com");
        ((User) userDetails).setUsername("testuser");
        ((User) userDetails).setRole(ROLE_USER);
    }

    @Test
    public void testExtractUserName() {
        String token = generateToken(userDetails);
        String userName = jwtService.extractUserName(token);
        assertEquals(userDetails.getUsername(), userName);
    }

    @Test
    public void testGenerateToken() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    public void testIsTokenValid() {
        String token = generateToken(userDetails);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    private String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", ((User) userDetails).getId());
        claims.put("email", ((User) userDetails).getEmail());
        claims.put("username", userDetails.getUsername());
        claims.put("role", ((User) userDetails).getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private String generateExpiredToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", ((User) userDetails).getId());
        claims.put("email", ((User) userDetails).getEmail());
        claims.put("username", userDetails.getUsername());
        claims.put("role", ((User) userDetails).getRole());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis() - expirationTime))
                .setExpiration(new Date(System.currentTimeMillis() - 1000))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSigningKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}