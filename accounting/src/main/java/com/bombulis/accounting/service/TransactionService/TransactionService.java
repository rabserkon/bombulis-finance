package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.entity.TransactionAccount;

import java.util.List;

public interface TransactionService {
    List<? extends TransactionAccount> findAllUserTransactions(Long userId);
    List<? extends TransactionAccount> findAllUserTransactions(Long userId, SearchCriteria searchCriteria);
}
