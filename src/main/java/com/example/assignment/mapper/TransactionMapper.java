package com.example.assignment.mapper;

import com.example.assignment.dto.TransactionDto;
import com.example.assignment.entity.Transaction;
import java.math.BigDecimal;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

  private final BTCPriceHistoryMapper btcPriceHistoryMapper;
  private final UsersMapper usersMapper;

  public TransactionMapper(BTCPriceHistoryMapper btcPriceHistoryMapper, UsersMapper usersMapper) {
    this.btcPriceHistoryMapper = btcPriceHistoryMapper;
    this.usersMapper = usersMapper;
  }

  public TransactionDto toDto(
      Transaction transaction, BigDecimal usdBalanceBefore, BigDecimal btcBalanceBefore) {
    return TransactionDto.builder()
        .id(transaction.getId())
        .userId(transaction.getUsers().getId())
        .btcAmount(transaction.getBtcAmount())
        .transactionTime(transaction.getTransactionTime())
        .transactionType(transaction.getTransactionType())
        .btcPriceHistory(btcPriceHistoryMapper.toDto(transaction.getBtcPriceHistory()))
        .users(usersMapper.toDto(transaction.getUsers()))
        .usdBalanceBefore(usdBalanceBefore)
        .btcBalanceBefore(btcBalanceBefore)
        .usdBalanceAfter(transaction.getUsers().getWallet().getUsdBalance())
        .btcBalanceAfter(transaction.getUsers().getWallet().getBtcBalance())
        .build();
  }
}
