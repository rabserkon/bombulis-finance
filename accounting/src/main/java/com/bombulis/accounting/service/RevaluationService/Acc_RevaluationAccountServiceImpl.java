package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_RateDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyService;
import com.bombulis.accounting.service.RevaluationService.RevaluationProcessors.Acc_RevaluationProcessor;
import com.bombulis.accounting.service.RevaluationService.RevaluationProcessors.Acc_RevaluationProcessorFactory;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class Acc_RevaluationAccountServiceImpl implements Acc_RevaluationAccountService {

    private Acc_CurrencyService currencyService;
    private Acc_ExchangeRateService exchangeRateService;
    private Acc_RevaluationProcessorFactory revaluationProcessorFactory;


    @Override
    public <T extends Acc_BalanceDTO> List<T> getRevaluationBalanceAccountList(List<Acc_Account> accountList, Acc_Currency revaluationCurrency, Date date) throws Acc_AccountOtherType, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_ServerDataAssetsException, Acc_CurrencyRateException {
        List<T> balanceList = new ArrayList<>();
        Map<String, Acc_RateDTO> cryptocurrencyRates = exchangeRateService.getExchangeRate(revaluationCurrency, currencyService.getAllCurrencies(), date);
        Map<String, Acc_RateDTO> currencyRates = exchangeRateService.getExchangeRate(revaluationCurrency, currencyService.getAllCurrencies(), date);
        for (Acc_Account account : accountList){
            Acc_RevaluationProcessor processor = revaluationProcessorFactory.getProcessor(account.getType());
            T balanceDTO = processor.processRevaluationBalance(account,revaluationCurrency,currencyRates);
            balanceList.add(balanceDTO);
        }
        return balanceList;
    }

    @Override
    public List<Acc_Account> getRevaluationAccountList(List<Acc_Account> accountList, String currencyISO, Date date) throws Acc_AccountOtherType, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_ServerDataAssetsException, Acc_CurrencyNonFound, Acc_CurrencyRateException {
        List<Acc_Account> balanceList = new ArrayList<>();
        Acc_Currency revaluationCurrency = currencyService.findCurrencyByIsoCode(currencyISO);
        Map<String, Acc_RateDTO> cryptocurrencyRates = exchangeRateService.getExchangeRate(revaluationCurrency, currencyService.getAllCurrencies(), date);
        Map<String, Acc_RateDTO> currencyRates = exchangeRateService.getExchangeRate(revaluationCurrency, currencyService.getAllCurrencies(), date);
        for (Acc_Account account : accountList){
            Acc_RevaluationProcessor processor = revaluationProcessorFactory.getProcessor(account.getType());
            Acc_Account revaluationAccount = processor.processRevaluationAccount(account,revaluationCurrency,currencyRates);
            balanceList.add(revaluationAccount);
        }
        return balanceList;
    }

    @Autowired
    public void setCurrencyService(Acc_CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setRevaluationProcessorFactory(Acc_RevaluationProcessorFactory revaluationProcessorFactory) {
        this.revaluationProcessorFactory = revaluationProcessorFactory;
    }

    @Autowired
    public void setExchangeRateService(Acc_ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }
}
