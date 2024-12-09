package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.dto.Acc_AccountEditDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;

public interface Acc_AccountProcessor {
    public <T extends Acc_Account> T processCreateAccount(Acc_AccountDTO accountDTO, Acc_User user) throws Acc_CurrencyNonFound, Acc_AccountException;
    public <T extends Acc_Account> T processEditAccount(Acc_AccountEditDTO accountDTO, Long userId) throws Acc_CurrencyNonFound;
    public Acc_AccountType getAccountType();
}
