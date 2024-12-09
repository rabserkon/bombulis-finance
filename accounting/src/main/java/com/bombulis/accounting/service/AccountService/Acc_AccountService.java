package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.dto.Acc_AccountEditDTO;
import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.UserService.Acc_UserException;

import java.util.Collection;

public interface Acc_AccountService {
    <T extends Acc_Account> T findAccount(Long accountId, Long userId) throws Acc_AccountNonFound, Acc_AccountTypeMismatchException;
    <T extends Acc_Account> T createAccount(Acc_AccountDTO accountDTO, Long userId) throws Acc_CurrencyNonFound, Acc_UserException, Acc_AccountException;
    <T extends Acc_Account> T deleteAccount(Long accountId, Long userId) throws Acc_AccountException;
    <T extends Acc_Account> Collection<T> findUserAccounts(Long userId) throws Acc_AccountNonFound;
    <T extends Acc_Account> T editAccount(Acc_AccountEditDTO accountDTO, Long userId) throws Acc_AccountNonFound;
    <T extends Acc_Account> T setBalance(Long accountId, Long userId, Acc_BalanceDTO balanceDTO) throws Acc_AccountNonFound;


}
