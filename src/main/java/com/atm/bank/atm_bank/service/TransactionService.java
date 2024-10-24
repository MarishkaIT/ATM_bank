package com.atm.bank.atm_bank.service;

import com.atm.bank.atm_bank.entity.Account;
import com.atm.bank.atm_bank.entity.Transaction;
import com.atm.bank.atm_bank.entity.TransactionType;
import com.atm.bank.atm_bank.exception.InsufficientBalanceException;
import com.atm.bank.atm_bank.repository.AccountRepository;
import com.atm.bank.atm_bank.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;
    private NotificationService notificationService;

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

    public Transaction processTransaction(Transaction transaction, String lang) {
        Account account = accountRepository.findById(transaction.getAccount().getId()).orElseThrow();
        if (transaction.getType() == TransactionType.DEPOSIT) {
            account.setAccountBalance(account.getAccountBalance() + transaction.getAmount());
            notificationService.sendNotification(account.getEmail(), "Deposit Notification", "Your account has been credited " +
                    transaction.getAmount() + " to the account " + transaction.getAccount(), lang);
        } else if (transaction.getType() == TransactionType.WITHDRAWAL) {
            if (account.getAccountBalance() >= transaction.getAmount()) {
                account.setAccountBalance(account.getAccountBalance() - transaction.getAmount());
                notificationService.sendNotification(account.getEmail(), "Withdrawal Notification", "Your account has been debited with " +
                        transaction.getAmount() + " to the account " + transaction.getAccount(), lang);
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
            notificationService.sendNotification(account.getEmail(), "Transfer Notification", "Your have transferred " +
                    transaction.getAmount() + " to the account " + transaction.getDestinationAccountId(), lang);
            notificationService.sendNotification(account.getEmail(), "Transfer Notification", "Your have received " +
                    transaction.getAmount() + " from account " + account.getId(), lang);
        }else if (transaction.getType() == TransactionType.BALANCE_INQUIRY) {
            transaction.setResponseCode("00");
            transaction.setResponseMessage("Balance inquiry successful");
            transaction.setBalance(account.getAccountBalance());
        }
        accountRepository.save(account);
        return transaction;
    }

    public List<Transaction> getTransactionsByAccountId(Long accountId) {
        return transactionRepository.findByAccount_Id(accountId);
    }
}
