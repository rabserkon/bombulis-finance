package com.bombulis.accounting.service.AccountService;

import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.entity.Acc_FinancingSource;
import com.bombulis.accounting.entity.Acc_WithdrawalDestination;
import com.bombulis.accounting.repository.Acc_AccountRepository;
import com.bombulis.accounting.repository.Acc_CurrencyRepository;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyService;
import com.bombulis.accounting.service.UserService.Acc_NotFoundUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Acc_FinancingSourcesService implements Acc_FinancingSourceService {

    private Acc_CurrencyService currencyService;
    private Acc_AccountRepository accountRepository;

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
        Acc_Currency currency = currencyService.findCurrencyByIsoCode(currencyCode);
        return currency;
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
