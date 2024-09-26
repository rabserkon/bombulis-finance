package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.dto.AccountEditDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.entity.FinancingSource;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.repository.CurrencyRepository;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.AccountService.exception.AccountException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DepositAccountProcessor implements AccountProcessor{
    @Autowired
    private CurrencyRepository currencyRepository;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public <T extends Account> T processCreateAccount(AccountDTO accountDTO, User user) throws CurrencyNonFound, AccountException {
        Currency currency = currencyRepository.findCurrencyByIsoCode(accountDTO.getCurrency())
                .orElseThrow(()->new CurrencyNonFound("Currency with code " + accountDTO.getCurrency() + " not found"));
        FinancingSource account = new FinancingSource(accountDTO.getName(),currency,user);
        account.setDescription(accountDTO.getDescription());
        account.setType(AccountType.DEPOSIT.name().toUpperCase());
        return (T) accountRepository.save(account);
    }

    @Override
    public <T extends Account> T processEditAccount(AccountEditDTO accountDTO, Long userId) throws CurrencyNonFound {
        return null;
    }

    @Override
    public AccountType getAccountType() {
        return AccountType.DEPOSIT;
    }
}
