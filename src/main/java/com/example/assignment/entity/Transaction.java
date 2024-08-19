package com.example.assignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "User cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private Users users;

    @NotNull(message = "BTC price history cannot be null")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "btc_price_history_id", nullable = false)
    @ToString.Exclude
    private BTCPriceHistory btcPriceHistory;

    @Min(value = 0, message = "BTC amount must be greater than or equal to 0")
    @Column(nullable = false)
    private double btcAmount;

    @NotNull(message = "Transaction time cannot be null")
    @PastOrPresent(message = "Transaction time must be in the past or present")
    @Column(nullable = false)
    private LocalDateTime transactionTime;

    @NotNull(message = "Transaction type cannot be null")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType transactionType;

    public Transaction(Users users, BTCPriceHistory btcPriceHistory, double btcAmount, LocalDateTime transactionTime, TransactionType transactionType) {
        this.users = users;
        this.btcPriceHistory = btcPriceHistory;
        this.btcAmount = btcAmount;
        this.transactionTime = transactionTime;
        this.transactionType = transactionType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transaction that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
