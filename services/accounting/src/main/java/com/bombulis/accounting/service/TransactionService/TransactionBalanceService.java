package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.TransactionDTO;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;

public interface TransactionBalanceService {

    TransactionAccount createTransaction(TransactionDTO transactionDTO,Long userId) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException;
    BalanceDTO balanceReconciliation(Long accountId, Long userId) throws AccountNonFound, AccountTypeMismatchException, AccountOtherType, CurrencyMismatchException;
    BalanceDTO calculateBalance(Long accountId, Long userId) throws CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound;
    <T extends CurrencyAccount> T calculateBalance(T account,  Long userId) throws CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound;
}
