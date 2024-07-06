package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.dto.AccountEditDTO;
import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.service.AccountService.exception.AccountException;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import com.bombulis.accounting.service.UserService.UserException;

import java.util.Collection;

public interface AccountService {
    <T extends Account> T findAccount(Long accountId, Long userId) throws AccountNonFound, AccountTypeMismatchException;
    <T extends Account> T createAccount(AccountDTO accountDTO, Long userId) throws CurrencyNonFound, UserException, AccountOtherType;
    <T extends Account> T deleteAccount(Long accountId, Long userId) throws AccountException;
    <T extends Account> Collection<T> findUserAccounts(Long userId) throws AccountNonFound;
    <T extends Account> T editAccount(AccountEditDTO accountDTO, Long userId) throws AccountNonFound;
    <T extends Account> T setBalance(Long accountId, Long userId, BalanceDTO balanceDTO) throws AccountNonFound;


}
