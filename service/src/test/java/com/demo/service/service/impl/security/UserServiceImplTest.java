package com.demo.service.service.impl.security;

import com.demo.service.model.User;
import com.demo.service.model.enums.Role;
import com.demo.service.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(1L, "test@example.com", "testUser", "password", Role.ROLE_USER);
    }

    @Test
    void testCreateUser() {
        // Подготовка данных
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(false);
        when(userRepository.save(testUser)).thenReturn(testUser);

        // Вызов тестируемого метода
        User createdUser = userService.create(testUser);

        // Проверка результатов
        assertNotNull(createdUser);
        assertEquals(testUser, createdUser);
    }

    @Test
    void testCreateUserWithExistingEmail() {
        // Подготовка данных
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(true);

        // Вызов тестируемого метода и проверка исключения
        assertThrows(RuntimeException.class, () -> userService.create(testUser));
    }

    @Test
    void testCreateUserWithExistingUsername() {
        // Подготовка данных
        when(userRepository.existsByEmail(testUser.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(testUser.getUsername())).thenReturn(true);

        // Вызов тестируемого метода и проверка исключения
        assertThrows(RuntimeException.class, () -> userService.create(testUser));
    }

    @Test
    void testGetByUsername() {
        // Подготовка данных
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Вызов тестируемого метода
        User foundUser = userService.getByUsername(testUser.getUsername());

        // Проверка результатов
        assertNotNull(foundUser);
        assertEquals(testUser, foundUser);
    }

    @Test
    void testGetByUsernameNotFound() {
        // Подготовка данных
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        // Вызов тестируемого метода и проверка исключения
        assertThrows(RuntimeException.class, () -> userService.getByUsername(testUser.getUsername()));
    }

    @Test
    void testUserDetailsService() {
        // Подготовка данных
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Вызов тестируемого метода
        UserDetailsService userDetailsService = userService.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(testUser.getUsername());

        // Проверка результатов
        assertNotNull(userDetails);
        assertEquals(testUser.getUsername(), userDetails.getUsername());
    }

    @Test
    void testGetCurrentUser() {
        // Подготовка данных
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(testUser.getUsername());
        when(userRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        // Вызов тестируемого метода
        User currentUser = userService.getCurrentUser();

        // Проверка результатов
        assertNotNull(currentUser);
        assertEquals(testUser, currentUser);
    }
}