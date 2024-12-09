package com.bombulis.accounting.service.AccountService.AccountProcessors;

import com.bombulis.accounting.dto.Acc_AccountDTO;
import com.bombulis.accounting.dto.Acc_AccountEditDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Acc_CurrencyAccountProcessor implements Acc_AccountProcessor {

    private Acc_CurrencyRepository currencyRepository;
    private Acc_AccountRepository accountRepository;

    @Override
    public <T extends Acc_Account> T processCreateAccount(Acc_AccountDTO accountDTO, Acc_User user) throws Acc_CurrencyNonFound, Acc_AccountException {
        Acc_CurrencyAccount account = new Acc_CurrencyAccount(accountDTO.getName(), user);
        Acc_Currency currency = currencyRepository.findCurrencyByIsoCode(accountDTO.getCurrency())
                .orElseThrow(()->new Acc_CurrencyNonFound("Currency with code " + accountDTO.getCurrency() + " not found"));
        if (!("CASH".equals(accountDTO.getSubType()) || "BANK".equals(accountDTO.getSubType()))) {
            throw new Acc_AccountException("Неподходящий тип счета: " + accountDTO.getSubType());
        }
        account.setCurrency(currency);
        account.setDescription(accountDTO.getDescription());
        account.setType(accountDTO.getType());
        account.setSubAccount(accountDTO.getSubType());
        return (T) accountRepository.save(account);
    }

    @Override
    public <T extends Acc_Account> T processEditAccount(Acc_AccountEditDTO accountDTO, Long userId) throws Acc_CurrencyNonFound {
        return null;
    }


    @Override
    public Acc_AccountType getAccountType() {
        return Acc_AccountType.CURRENCY;
    }

    @Autowired
    public void setCurrencyRepository(Acc_CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
}
