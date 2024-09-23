package com.atm.bank.atm_bank.service;

import com.atm.bank.atm_bank.entity.Account;
import com.atm.bank.atm_bank.entity.Transaction;
import com.atm.bank.atm_bank.entity.TransactionType;
import com.atm.bank.atm_bank.exception.InsufficientBalanceException;
import com.atm.bank.atm_bank.repository.AccountRepository;
import com.atm.bank.atm_bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow();
    }

    public Transaction updateTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    public void processTransaction(Transaction transaction) {
        Account account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow();
        if (transaction.getType() == TransactionType.DEPOSIT) {
            account.setAccountBalance(account.getAccountBalance() + transaction.getAmount());
        } else if (transaction.getType() == TransactionType.WITHDRAWAL) {
            if (account.getAccountBalance() >= transaction.getAmount()) {
                account.setAccountBalance(account.getAccountBalance() - transaction.getAmount());
            }else {
                throw new InsufficientBalanceException();
            }
        } else if (transaction.getType() == TransactionType.TRANSFER) {
            Account destinationAccount = accountRepository.findById(transaction.getDestinationAccountId()).orElseThrow();
            if (account.getAccountBalance() < transaction.getAmount()) {
                throw new InsufficientBalanceException();
            }
            account.setAccountBalance(account.getAccountBalance() - transaction.getAmount());
            destinationAccount.setAccountBalance(destinationAccount.getAccountBalance() + transaction.getAmount());
            accountRepository.save(account);
            accountRepository.save(destinationAccount);
        }else if (transaction.getType() == TransactionType.BALANCE_INQUIRY) {
            transaction.setResponseCode("00");
            transaction.setResponseMessage("Balance inquiry successful");
            transaction.setBalance(account.getAccountBalance());
        }
        accountRepository.save(account);
    }
}
