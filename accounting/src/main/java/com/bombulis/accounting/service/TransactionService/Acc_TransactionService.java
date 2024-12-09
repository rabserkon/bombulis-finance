package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.entity.Acc_TransactionAccount;

import java.util.List;

public interface Acc_TransactionService {
    List<? extends Acc_TransactionAccount> findAllUserTransactions(Long userId);
    List<? extends Acc_TransactionAccount> findAllUserTransactions(Long userId, Acc_SearchCriteria searchCriteria);
}
