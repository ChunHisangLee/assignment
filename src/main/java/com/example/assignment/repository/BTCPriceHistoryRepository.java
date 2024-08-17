package com.example.assignment.repository;

import com.example.assignment.entity.BTCPriceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BTCPriceHistoryRepository extends JpaRepository<BTCPriceHistory, Long> {
}
