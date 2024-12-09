package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.UserService.Acc_NotFoundUser;

import java.util.List;

public interface Acc_FinancingSourceService {
    List<? extends Acc_Account> getListDepositAccount(Long userId) throws Acc_NotFoundUser, Acc_CurrencyNonFound;

    List<? extends Acc_Account> getListWithdrawalAccount(Long userId) throws Acc_NotFoundUser, Acc_CurrencyNonFound;
}
