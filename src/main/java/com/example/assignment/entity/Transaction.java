package com.example.assignment.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private Users users;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "btc_price_history_id", nullable = false)
    private BTCPriceHistory btcPriceHistory;

    @Column(nullable = false)
    private double btcAmount;

    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;
}
