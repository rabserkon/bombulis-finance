package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.dto.Acc_AccountEditDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.entity.Acc_WithdrawalDestination;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Acc_WithdrawalAccountProcessor implements Acc_AccountProcessor {

    private Acc_CurrencyService currencyService;
    private Acc_AccountRepository accountRepository;

    @Override
    public <T extends Acc_Account> T processCreateAccount(Acc_AccountDTO accountDTO, Acc_User user) throws Acc_CurrencyNonFound, Acc_AccountException {
        Acc_Currency currency = currencyService.findCurrencyByIsoCode(accountDTO.getCurrency());
        Acc_WithdrawalDestination account = new Acc_WithdrawalDestination(accountDTO.getName(),currency,user);
        account.setDescription(accountDTO.getDescription());
        account.setType(Acc_AccountType.WITHDRAWAL.name().toUpperCase());
        return (T) accountRepository.save(account);
    }

    @Override
    public <T extends Acc_Account> T processEditAccount(Acc_AccountEditDTO accountDTO, Long userId) throws Acc_CurrencyNonFound {
        return null;
    }

    @Override
    public Acc_AccountType getAccountType() {
        return Acc_AccountType.WITHDRAWAL;
    }

    @Autowired
    public void setCurrencyService(Acc_CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
