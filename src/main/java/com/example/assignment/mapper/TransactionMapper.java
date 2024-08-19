package com.example.assignment.mapper;

import com.example.assignment.dto.TransactionDTO;
import com.example.assignment.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {

    private final BTCPriceHistoryMapper btcPriceHistoryMapper;
    private final UsersMapper usersMapper;

    public TransactionMapper(BTCPriceHistoryMapper btcPriceHistoryMapper, UsersMapper usersMapper) {
        this.btcPriceHistoryMapper = btcPriceHistoryMapper;
        this.usersMapper = usersMapper;
    }

    public TransactionDTO convertToDto(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getBtcAmount(),
                transaction.getTransactionTime(),
                transaction.getTransactionType().name(),
                btcPriceHistoryMapper.convertToDto(transaction.getBtcPriceHistory()),
                usersMapper.convertToDto(transaction.getUsers())
        );
    }
}
