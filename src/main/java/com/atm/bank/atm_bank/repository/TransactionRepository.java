package com.atm.bank.atm_bank.repository;

import com.atm.bank.atm_bank.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}