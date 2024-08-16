package com.example.assignment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double usdBalance;

    private double btcBalance;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private Users users;
}
