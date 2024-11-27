package ru.t1.java.demo.service.impl.security;

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
import ru.t1.java.demo.model.User;
import ru.t1.java.demo.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setUsername("testuser");
        user.setPassword("password");
    }

    @Test
    public void testCreateUser() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.create(user);

        assertNotNull(createdUser);
        assertEquals(user.getEmail(), createdUser.getEmail());
        assertEquals(user.getUsername(), createdUser.getUsername());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    public void testCreateUserWithExistingUsername() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    public void testGetByUsername() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User foundUser = userService.getByUsername(user.getUsername());

        assertNotNull(foundUser);
        assertEquals(user.getEmail(), foundUser.getEmail());
        assertEquals(user.getUsername(), foundUser.getUsername());
    }

    @Test
    public void testGetByUsernameNotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getByUsername(user.getUsername()));
    }

    @Test
    public void testUserDetailsService() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        UserDetailsService userDetailsService = userService.userDetailsService();
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());

        assertNotNull(userDetails);
        assertEquals(user.getUsername(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    public void testGetCurrentUser() {
        // Создаем мок для SecurityContext и Authentication
        Authentication authentication = mock(Authentication.class);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getName()).thenReturn(user.getUsername());
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        User currentUser = userService.getCurrentUser();

        assertNotNull(currentUser);
        assertEquals(user.getEmail(), currentUser.getEmail());
        assertEquals(user.getUsername(), currentUser.getUsername());
    }
}