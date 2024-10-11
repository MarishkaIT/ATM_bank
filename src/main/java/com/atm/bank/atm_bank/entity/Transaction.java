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

    @Enumerated(EnumType.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private TransactionType type;

    @ManyToOne
    private Account account;

    private String responseCode;
    private String responseMessage;

    private Long DestinationAccountId;
    private Double balance;
}
