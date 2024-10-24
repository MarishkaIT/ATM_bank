package com.atm.bank.atm_bank.controller;

import com.atm.bank.atm_bank.entity.Account;
import com.atm.bank.atm_bank.entity.Transaction;
import com.atm.bank.atm_bank.entity.TransactionType;
import com.atm.bank.atm_bank.entity.User;
import com.atm.bank.atm_bank.exception.AuthenticationException;
import com.atm.bank.atm_bank.service.AccountService;
import com.atm.bank.atm_bank.service.TransactionService;
import com.atm.bank.atm_bank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> login(@RequestBody User user) {
       try {
           userService.authenticateUser(user.getUsername(), user.getPassword());
           return ResponseEntity.ok("success");
       }catch (AuthenticationException e) {
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
       }
    }

    @GetMapping("/accounts")
    public ResponseEntity<List<Account>> getAccounts() {
        List<Account> accounts = accountService.getAccounts();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/account/{id}")
    public ResponseEntity<Account> getAccount(@PathVariable Long id) {
        Account account = accountService.getAccount(id);
        return account != null ? ResponseEntity.ok(account) : ResponseEntity.notFound().build();
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestBody Transaction transaction, @RequestParam(defaultValue = "en") String lang) {
        transaction.setType(TransactionType.DEPOSIT);
        Transaction processedTransaction = transactionService.processTransaction(transaction, lang);
        return ResponseEntity.ok(processedTransaction);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestBody Transaction transaction, @RequestParam(defaultValue = "en") String lang) {
        transaction.setType(TransactionType.WITHDRAWAL);
        Transaction processedTransaction = transactionService.processTransaction(transaction, lang);
        return ResponseEntity.ok(processedTransaction);
    }


    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(@RequestBody Transaction transaction, @RequestParam(defaultValue = "en") String lang) {
       try {
           transaction.setType(TransactionType.TRANSFER);
           Transaction processedTransaction = transactionService.processTransaction(transaction, lang);
           return ResponseEntity.ok(processedTransaction);
       } catch (RuntimeException e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
       }

    }

    @PostMapping("/balance-inquiry")
    public ResponseEntity<Transaction> balanceInquiry(@RequestBody Transaction transaction, @RequestParam(defaultValue = "en") String lang) {
        transaction.setType(TransactionType.BALANCE_INQUIRY);
        Transaction processedTransaction = transactionService.processTransaction(transaction, lang);
        return ResponseEntity.ok(processedTransaction);
    }

    @GetMapping("/transactions/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactions(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }
}



















