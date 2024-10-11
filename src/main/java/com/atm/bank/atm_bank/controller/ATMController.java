package com.atm.bank.atm_bank.controller;

import com.atm.bank.atm_bank.entity.Account;
import com.atm.bank.atm_bank.entity.Transaction;
import com.atm.bank.atm_bank.entity.TransactionType;
import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.service.AccountService;
import com.atm.bank.atm_bank.service.TransactionService;
import com.atm.bank.atm_bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/atm")
public class ATMController {
    @Autowired
    private UserService userService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/login")
    public String login(@RequestBody User user) {
        if (userService.authenticateUser(user.getUsername(), user.getPassword())) {
            return "Login successful";
        }else {
            return "Login failed. Invalid username or password";
        }
    }

    @GetMapping("/accounts")
    public List<Account> getAccounts() {
        return accountService.getAccounts();
    }

    @GetMapping("/account/{id}")
    public Account getAccount(@PathVariable Long id) {
        return accountService.getAccount(id);
    }

    @PostMapping("/deposit")
    public Transaction deposit(@RequestBody Transaction transaction) {
        transaction.setType(TransactionType.DEPOSIT);
        return transactionService.processTransaction(transaction);
    }

    @PostMapping("/withdraw")
    public Transaction withdraw(@RequestBody Transaction transaction) {
        transaction.setType(TransactionType.WITHDRAWAL);
        return transactionService.processTransaction(transaction);
    }

    @PostMapping("/transfer")
    public Transaction transfer(@RequestBody Transaction transaction) {
        transaction.setType(TransactionType.TRANSFER);
        return transactionService.processTransaction(transaction);
    }

    @PostMapping("/balance-inquiry")
    public Transaction balanceInquiry(@RequestBody Transaction transaction) {
        transaction.setType(TransactionType.BALANCE_INQUIRY);
        return transactionService.processTransaction(transaction);
    }

    @GetMapping("/transactions/{accountId}")
    public List<Transaction> getTransactions(@PathVariable Long accountId) {
        return transactionService.getTransactionsByAccountId(accountId);
    }
}



















