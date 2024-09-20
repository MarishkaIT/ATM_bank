package com.atm.bank.atm_bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Double amount;

    private TransactionType type;

    @ManyToOne
    private Account account;

    private Date transactionDate;
}
