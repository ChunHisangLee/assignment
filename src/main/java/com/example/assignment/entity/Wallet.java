package com.example.assignment.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 0, message = "USD balance must be non-negative")
    @Column(nullable = false)
    private double usdBalance;

    @Min(value = 0, message = "BTC balance must be non-negative")
    @Column(nullable = false)
    private double btcBalance;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    @ToString.Exclude
    private Users users;

    public Wallet(double usdBalance, double btcBalance, Users users) {
        this.usdBalance = usdBalance;
        this.btcBalance = btcBalance;
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Wallet wallet)) return false;
        return Objects.equals(id, wallet.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
