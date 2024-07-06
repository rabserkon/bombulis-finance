package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.AccountRepository;
import com.bombulis.accounting.repository.CurrencyRepository;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.UserService.NotFoundUser;
import com.bombulis.accounting.service.UserService.UserException;
import com.bombulis.accounting.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DepositWithdrawalService implements FinancingSourceService {

    private CurrencyRepository currencyRepository;
    private AccountRepository accountRepository;
    private UserService userService;

    @Override
    public FinancingSource createFinancingSource(String name, String currencyCode, Long userId) throws UserException, CurrencyNonFound {
        User user = userService.findUserById(userId);
        Currency currency = this.findCurrency(currencyCode);
        Account account = new FinancingSource(name, currency, user);
        return (FinancingSource) accountRepository.save(account);
    }

    @Override
    public WithdrawalDestination createWithdrawalAccount(String name, String currencyCode, Long userId) throws UserException, CurrencyNonFound {
        User user = userService.findUserById(userId);
        Currency currency = this.findCurrency(currencyCode);
        Account account = new WithdrawalDestination(name, currency, user);
        return (WithdrawalDestination) accountRepository.save(account);
    }

    @Override
    public List<? extends Account> getListDepositAccount(Long userId) throws NotFoundUser, CurrencyNonFound {
        List<FinancingSource> accountList = accountRepository.findFinancingSourcesByUserUserIdAndDeletedFalse(userId);
        if (accountList ==  null){
            return new ArrayList<>();
        }
        return accountList;
    }

    @Override
    public List<? extends Account> getListWithdrawalAccount(Long userId) throws NotFoundUser, CurrencyNonFound {
        List<WithdrawalDestination> accountList = accountRepository.findWithdrawalDestinationsByUserUserIdAndDeletedFalse(userId);
        if (accountList ==  null){
            return new ArrayList<>();
        }
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
