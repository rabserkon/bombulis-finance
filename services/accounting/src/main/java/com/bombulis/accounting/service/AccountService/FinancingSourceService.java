package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import com.bombulis.accounting.service.UserService.UserException;

import java.util.List;

public interface FinancingSourceService {
    Account createFinancingSource(String name, String currencyCode, Long userId) throws UserException, CurrencyNonFound;

    Account createWithdrawalAccount(String name, String currencyCode, Long userId) throws UserException, CurrencyNonFound;

    List<? extends Account> getListDepositAccount(Long userId) throws NotFoundUser, CurrencyNonFound;

    List<? extends Account> getListWithdrawalAccount(Long userId) throws NotFoundUser, CurrencyNonFound;
}
