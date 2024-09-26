package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.dto.AccountDTO;
import com.bombulis.accounting.dto.AccountEditDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.repository.CurrencyRepository;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.AccountService.exception.AccountException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CurrencyAccountProcessor implements AccountProcessor {

    private CurrencyRepository currencyRepository;
    private AccountRepository accountRepository;

    @Override
    public <T extends Account> T processCreateAccount(AccountDTO accountDTO, User user) throws CurrencyNonFound, AccountException {
        CurrencyAccount account = new CurrencyAccount(accountDTO.getName(), user);
        Currency currency = currencyRepository.findCurrencyByIsoCode(accountDTO.getCurrency())
                .orElseThrow(()->new CurrencyNonFound("Currency with code " + accountDTO.getCurrency() + " not found"));
        if (!("CASH".equals(accountDTO.getSubType()) || "BANK".equals(accountDTO.getSubType()))) {
            throw new AccountException("Неподходящий тип счета: " + accountDTO.getSubType());
        }
        account.setCurrency(currency);
        account.setDescription(accountDTO.getDescription());
        account.setType(accountDTO.getType());
        account.setSubAccount(accountDTO.getSubType());
        return (T) accountRepository.save(account);
    }

    @Override
    public <T extends Account> T processEditAccount(AccountEditDTO accountDTO, Long userId) throws CurrencyNonFound {
        return null;
    }


    @Override
    public AccountType getAccountType() {
        return AccountType.CURRENCY;
    }

    @Autowired
    public void setCurrencyRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
