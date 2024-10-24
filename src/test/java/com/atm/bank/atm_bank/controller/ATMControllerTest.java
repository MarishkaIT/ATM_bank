package com.atm.bank.atm_bank.controller;

import com.atm.bank.atm_bank.entity.Account;
import com.atm.bank.atm_bank.entity.Transaction;
import com.atm.bank.atm_bank.entity.TransactionType;
import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.exception.AuthenticationException;
import com.atm.bank.atm_bank.service.AccountService;
import com.atm.bank.atm_bank.service.TransactionService;
import com.atm.bank.atm_bank.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import java.util.Collections;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ATMControllerTest {


    private MockMvc mockMvc;
    @Mock
    private AccountService accountService;
    @Mock
    private TransactionService transactionService;
    @InjectMocks
    private ATMController atmController;
    @Mock
    private UserService userService;

    @BeforeEach
    public void setUp() {
        openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(atmController).build();
    }

    @Test
    public void loginSuccess() throws Exception {
        User user = new User();
        user.setUsername("testUser ");
        user.setPassword("testPassword");

        doNothing().when(userService).authenticateUser("testUser ", "testPassword");

        mockMvc.perform(post("/atm/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"testUser \", \"password\":\"testPassword\"}"))
                .andExpect(status().isOk())
                .andExpect(content().string("success"));
    }

    @Test
    public void loginFail() throws Exception {
        User user = new User();
        user.setUsername("testUser ");
        user.setPassword("testPassword");

        doThrow(new AuthenticationException("Invalid username or password"))
                .when(userService).authenticateUser("testUser ", "testPassword");

        mockMvc.perform(post("/atm/login")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testUser \", \"password\":\"testPassword\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getAccountsTest() throws Exception {
        List<Account> accounts = Collections.singletonList(new Account());
        when(accountService.getAccounts()).thenReturn(accounts);

        mockMvc.perform(get("/atm/accounts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void getAccountTest() throws Exception {
        Long accountId = 1L;
        Account account = new Account();
        when(accountService.getAccount(accountId)).thenReturn(account);

        mockMvc.perform(get("/atm/account/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void getTransactionsTest() throws Exception {
        Long accountId = 1L;
        List<Transaction> transactions = Collections.singletonList(new Transaction());

        when(transactionService.getTransactionsByAccountId(accountId)).thenReturn(transactions);

        mockMvc.perform(get("/atm/transactions/{id}", accountId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());

    }

    @Test
    public void depositTest() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.DEPOSIT);
        when(transactionService.processTransaction(any(Transaction.class), any(String.class))).thenReturn(transaction);

        mockMvc.perform(post("/atm/deposit?lang=ukr")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void withdrawTest() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        when(transactionService.processTransaction(any(Transaction.class), any(String.class))).thenReturn(transaction);

        mockMvc.perform(post("/atm/withdraw?lang=ukr")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void transferTest() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        when(transactionService.processTransaction(any(Transaction.class), any(String.class))).thenReturn(transaction);

        mockMvc.perform(post("/atm/transfer?lang=ukr")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\":100}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void transferFail() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.TRANSFER);
        when(transactionService.processTransaction(any(Transaction.class), any(String.class))).thenThrow(new RuntimeException("Transfer failed"));

        mockMvc.perform(post("/atm/transfer?lang=ukr")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"type\":\"TRANSFER\"}"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void balanceInquiryTest() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setType(TransactionType.BALANCE_INQUIRY);
        when(transactionService.processTransaction(any(Transaction.class), any(String.class))).thenReturn(transaction);

        mockMvc.perform(post("/atm/balance-inquiry?lang=ukr")
        .contentType(MediaType.APPLICATION_JSON)
                .content("{\"accountId\":1}"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}





