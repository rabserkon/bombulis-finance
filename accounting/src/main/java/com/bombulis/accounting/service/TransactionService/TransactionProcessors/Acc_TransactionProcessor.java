package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.Acc_Transaction;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;

public interface Acc_TransactionProcessor {
    Acc_Transaction processCreateTransaction(Acc_TransactionDTO transactionDTO, Acc_User user) throws Acc_AccountException;
    Acc_BalanceDTO processCalculateBalance(Acc_TransactionAccount transactionAccount, Acc_BalanceDTO balanceDTO);
    Acc_TransactionType getType();
}
