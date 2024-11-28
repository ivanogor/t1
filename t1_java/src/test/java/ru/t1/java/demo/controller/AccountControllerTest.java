package ru.t1.java.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.t1.java.demo.dto.AccountDto;
import ru.t1.java.demo.exception.AccountNotFoundException;
import ru.t1.java.demo.jwt.JwtAuthenticationFilter;
import ru.t1.java.demo.model.enums.AccountStatus;
import ru.t1.java.demo.model.enums.AccountType;
import ru.t1.java.demo.service.AccountService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = AccountController.class, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class)
})
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    private AccountDto accountDto;

    @BeforeEach
    public void setUp() {
        accountDto = AccountDto.builder()
                .id(1L)
                .accountType(AccountType.DEBIT)
                .balance(new BigDecimal("1000.00"))
                .clientId(1L)
                .accountStatus(AccountStatus.OPEN)
                .accountId(UUID.randomUUID())
                .frozenAmount(new BigDecimal("0.00"))
                .build();
    }

    @Test
    public void testCreateAccount() throws Exception {
        when(accountService.createAccount(any(AccountDto.class))).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountType\":\"DEBIT\",\"balance\":1000.00,\"clientId\":1,\"accountStatus\":\"OPEN\",\"accountId\":\"" + accountDto.getAccountId() + "\",\"frozenAmount\":0.00}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("DEBIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountStatus").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(accountDto.getAccountId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.frozenAmount").value(0.00));
    }

    @Test
    public void testGetAccountById() throws Exception {
        when(accountService.getAccount(1L)).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("DEBIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountStatus").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(accountDto.getAccountId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.frozenAmount").value(0.00));
    }

    @Test
    public void testGetAccountByIdNotFound() throws Exception {
        when(accountService.getAccount(1L)).thenThrow(new AccountNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllAccounts() throws Exception {
        List<AccountDto> accounts = Collections.singletonList(accountDto);
        when(accountService.getAccounts()).thenReturn(accounts);

        mockMvc.perform(MockMvcRequestBuilders.get("/accounts"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountType").value("DEBIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].balance").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].clientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountStatus").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").value(accountDto.getAccountId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].frozenAmount").value(0.00));
    }

    @Test
    public void testUpdateAccount() throws Exception {
        when(accountService.updateAccount(eq(1L), any(AccountDto.class))).thenReturn(accountDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountType\":\"DEBIT\",\"balance\":1000.00,\"clientId\":1,\"accountStatus\":\"OPEN\",\"accountId\":\"" + accountDto.getAccountId() + "\",\"frozenAmount\":0.00}"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountType").value("DEBIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(1000.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.clientId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountStatus").value("OPEN"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(accountDto.getAccountId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.frozenAmount").value(0.00));
    }

    @Test
    public void testUpdateAccountNotFound() throws Exception {
        when(accountService.updateAccount(eq(1L), any(AccountDto.class))).thenThrow(new AccountNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.put("/accounts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"accountType\":\"DEBIT\",\"balance\":1000.00,\"clientId\":1,\"accountStatus\":\"OPEN\",\"accountId\":\"" + accountDto.getAccountId() + "\",\"frozenAmount\":0.00}"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteAccount() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteAccountNotFound() throws Exception {
        Mockito.doThrow(new AccountNotFoundException(1L)).when(accountService).deleteAccount(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}