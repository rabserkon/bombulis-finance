package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.Acc_Transaction;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;

public interface Acc_TransactionProcessor {
    Acc_Transaction processCreateTransaction(Acc_TransactionDTO transactionDTO, Acc_User user) throws Acc_AccountException;
    Acc_BalanceDTO processCalculateBalance(Acc_TransactionAccount transactionAccount, Acc_BalanceDTO balanceDTO);
    public Acc_TransactionType getType();
}
