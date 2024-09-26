package com.bombulis.accounting.service.TransactionService.TransactionProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;

public interface TransactionProcessor {
    TransactionAccount processCreateTransaction(TransactionDTO transactionDTO, User user) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException;
    BalanceDTO processCalculateBalance(TransactionAccount transactionAccount, BalanceDTO balanceDTO);
    public TransactionType getType();
}
