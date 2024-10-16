package com.atm.bank.atm_bank.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "transaction")
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

    @Column(name = "response_code")
    private String responseCode;
    @Column(name = "response_message")
    private String responseMessage;

    @Column(name = "destination_account_id")
    private Long DestinationAccountId;
    private Double balance;
}
