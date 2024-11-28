package com.demo.service.controller;

import com.demo.service.dto.JwtAuthenticationResponseDto;
import com.demo.service.dto.SignInRequestDto;
import com.demo.service.dto.SignUpRequestDto;
import com.demo.service.jwt.AuthenticationService;
import com.demo.service.service.impl.security.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtServiceImpl jwtServiceImpl; // Добавляем мок для JwtServiceImpl

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSignUp() throws Exception {
        // Подготовка данных
        SignUpRequestDto signUpRequest = SignUpRequestDto.builder()
                .username("testUser")
                .email("test@example.com")
                .password("password")
                .build();

        JwtAuthenticationResponseDto responseDto = JwtAuthenticationResponseDto.builder()
                .token("jwtToken")
                .build();

        // Мокирование сервиса
        when(authenticationService.signUp(any(SignUpRequestDto.class))).thenReturn(responseDto);

        // Выполнение запроса
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("jwtToken"));
    }

    @Test
    public void testSignIn() throws Exception {
        // Подготовка данных
        SignInRequestDto signInRequest = SignInRequestDto.builder()
                .username("testUser")
                .password("password")
                .build();

        JwtAuthenticationResponseDto responseDto = JwtAuthenticationResponseDto.builder()
                .token("jwtToken")
                .build();

        // Мокирование сервиса
        when(authenticationService.signIn(any(SignInRequestDto.class))).thenReturn(responseDto);

        // Выполнение запроса
        mockMvc.perform(MockMvcRequestBuilders.post("/auth/sign-in")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signInRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("jwtToken"));
    }
}