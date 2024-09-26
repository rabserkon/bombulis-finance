package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.repository.CurrencyRepository;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import com.bombulis.accounting.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FinancingSourcesService implements FinancingSourceService {

    private CurrencyRepository currencyRepository;
    private AccountRepository accountRepository;
    private UserService userService;

    @Override
    public List<? extends Account> getListDepositAccount(Long userId) throws NotFoundUser, CurrencyNonFound {
        List<FinancingSource> accountList = accountRepository.findFinancingSourcesByUserUserIdAndDeletedFalse(userId);
        return accountList;
    }

    @Override
    public List<? extends Account> getListWithdrawalAccount(Long userId) throws NotFoundUser, CurrencyNonFound {
        List<WithdrawalDestination> accountList = accountRepository.findWithdrawalDestinationsByUserUserIdAndDeletedFalse(userId);
        return accountList;
    }


    private Currency findCurrency(String currencyCode) throws CurrencyNonFound {
        Currency currency = currencyRepository.findCurrencyByIsoCode(currencyCode)
                .orElseThrow(()-> new CurrencyNonFound("Currency with code " + currencyCode + " not found"));
        return currency;
    }

    @Autowired
    public void setCurrencyRepository(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Autowired
    public void setAccountRepository(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}
