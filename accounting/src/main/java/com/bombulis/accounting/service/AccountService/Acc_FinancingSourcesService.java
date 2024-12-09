package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.*;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.UserService.Acc_NotFoundUser;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Acc_FinancingSourcesService implements Acc_FinancingSourceService {

    private Acc_CurrencyRepository currencyRepository;
    private Acc_AccountRepository accountRepository;
    private Acc_UserService userService;

    @Override
    public List<? extends Acc_Account> getListDepositAccount(Long userId) throws Acc_NotFoundUser, Acc_CurrencyNonFound {
        List<Acc_FinancingSource> accountList = accountRepository.findFinancingSourcesByUserUserIdAndDeletedFalse(userId);
        return accountList;
    }

    @Override
    public List<? extends Acc_Account> getListWithdrawalAccount(Long userId) throws Acc_NotFoundUser, Acc_CurrencyNonFound {
        List<Acc_WithdrawalDestination> accountList = accountRepository.findWithdrawalDestinationsByUserUserIdAndDeletedFalse(userId);
        return accountList;
    }


    private Acc_Currency findCurrency(String currencyCode) throws Acc_CurrencyNonFound {
        Acc_Currency currency = currencyRepository.findCurrencyByIsoCode(currencyCode)
                .orElseThrow(()-> new Acc_CurrencyNonFound("Currency with code " + currencyCode + " not found"));
        return currency;
    }

    @Autowired
    public void setCurrencyRepository(Acc_CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Autowired
    public void setAccountRepository(Acc_AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Autowired
    public void setUserService(Acc_UserService userService) {
        this.userService = userService;
    }
}
