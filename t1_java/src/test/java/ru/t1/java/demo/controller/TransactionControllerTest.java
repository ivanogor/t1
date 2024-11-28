package ru.t1.java.demo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.t1.java.demo.dto.TransactionDto;
import ru.t1.java.demo.exception.TransactionNotFoundException;
import ru.t1.java.demo.model.enums.TransactionStatus;
import ru.t1.java.demo.service.TransactionService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    private ObjectMapper objectMapper;

    private TransactionDto transactionDto;


    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        transactionDto = TransactionDto.builder()
                .id(1L)
                .amount(new BigDecimal("100.00"))
                .accountId(1L)
                .transactionStatus(TransactionStatus.ACCEPTED)
                .createdAt(LocalDateTime.now())
                .transactionId(UUID.randomUUID())
                .build();
    }

    @Test
    public void testCreateTransaction() throws Exception {
        when(transactionService.createTransaction(any(TransactionDto.class))).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus").value("ACCEPTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId").isNotEmpty());
    }

    @Test
    public void testGetTransactionById() throws Exception {
        when(transactionService.getTransaction(1L)).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus").value("ACCEPTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId").isNotEmpty());
    }

    @Test
    public void testGetTransactionByIdNotFound() throws Exception {
        when(transactionService.getTransaction(1L)).thenThrow(new TransactionNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetAllTransactions() throws Exception {
        List<TransactionDto> transactions = Collections.singletonList(transactionDto);
        when(transactionService.getTransactions()).thenReturn(transactions);

        mockMvc.perform(MockMvcRequestBuilders.get("/transactions"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].amount").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].accountId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionStatus").value("ACCEPTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].transactionId").isNotEmpty());
    }

    @Test
    public void testUpdateTransaction() throws Exception {
        when(transactionService.updateTransaction(eq(1L), any(TransactionDto.class))).thenReturn(transactionDto);

        mockMvc.perform(MockMvcRequestBuilders.put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amount").value(100.00))
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountId").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionStatus").value("ACCEPTED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.transactionId").isNotEmpty());
    }

    @Test
    public void testUpdateTransactionNotFound() throws Exception {
        when(transactionService.updateTransaction(eq(1L), any(TransactionDto.class))).thenThrow(new TransactionNotFoundException(1L));

        mockMvc.perform(MockMvcRequestBuilders.put("/transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTransaction() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/1"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTransactionNotFound() throws Exception {
        Mockito.doThrow(new TransactionNotFoundException(1L)).when(transactionService).deleteTransaction(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/transactions/1"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}