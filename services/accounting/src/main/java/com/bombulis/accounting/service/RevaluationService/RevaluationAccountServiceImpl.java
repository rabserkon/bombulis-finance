package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.RateDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.CurrencyService;
import com.bombulis.accounting.service.RevaluationService.RevaluationProcessors.RevaluationProcessor;
import com.bombulis.accounting.service.RevaluationService.RevaluationProcessors.RevaluationProcessorFactory;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class RevaluationAccountServiceImpl implements RevaluationAccountService{

    private CurrencyService currencyService;
    private ExchangeRateService exchangeRateService;
    private RevaluationProcessorFactory revaluationProcessorFactory;


    @Override
    public <T extends BalanceDTO> List<T> getRevaluationAccountList(List<Account> accountList, Currency revaluationCurrency, Date date) throws AccountOtherType, CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound, ServerDataAssetsException {
        List<T> balanceList = new ArrayList<>();
        Map<String, RateDTO> cryptocurrencyRates = exchangeRateService.getExchangeRate(revaluationCurrency, currencyService.getAllCurrencies(), date);
        Map<String, RateDTO> currencyRates = exchangeRateService.getExchangeRate(revaluationCurrency, currencyService.getAllCurrencies(), date);
        for (Account account : accountList){
            RevaluationProcessor processor = revaluationProcessorFactory.getProcessor(account.getType());
            T balanceDTO = processor.processRevaluationAccount(account,revaluationCurrency,currencyRates);
            balanceList.add(balanceDTO);
        }
        return balanceList;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setRevaluationProcessorFactory(RevaluationProcessorFactory revaluationProcessorFactory) {
        this.revaluationProcessorFactory = revaluationProcessorFactory;
    }

    @Autowired
    public void setExchangeRateService(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;
    }
}
