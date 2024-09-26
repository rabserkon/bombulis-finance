package com.bombulis.accounting.service.TransactionService.BalanceProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;

public interface BalanceProcessor  {
    BalanceDTO process(Account account) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException;
    public AccountType getType();
}
