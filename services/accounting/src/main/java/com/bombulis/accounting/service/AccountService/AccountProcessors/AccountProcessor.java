package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.dto.AccountEditDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;

public interface AccountProcessor {
    public <T extends Account> T processCreateAccount(AccountDTO accountDTO, User user) throws CurrencyNonFound;
    public <T extends Account> T processEditAccount(AccountEditDTO accountDTO, Long userId) throws CurrencyNonFound;
    public AccountType getAccountType();
}
