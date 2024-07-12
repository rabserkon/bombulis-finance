package com.bombulis.accounting.service.RevaluationService.RevaluationProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.RateDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.entity.CurrencyAccount;
import com.bombulis.accounting.service.AccountService.AccountType;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class CurrencyRevaluationProcessor implements RevaluationProcessor{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public <T extends BalanceDTO> T processRevaluationBalance(Account account, Currency revaluationCurrency, Map<String, RateDTO> currencyRates) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType {
        CurrencyAccount currencyAccount = (CurrencyAccount) account;
        RateDTO rateDTO = currencyRates.get(currencyAccount.getCurrency().getIsoCode());
        if (rateDTO == null && currencyAccount.getCurrency().equals(revaluationCurrency)){
            rateDTO = new RateDTO(
                    revaluationCurrency.getIsoCode(),
                    revaluationCurrency.getIsoCode(),
                    revaluationCurrency.getIsoCode(),
                    1.0000
            );
        }
        BigDecimal revaluationBalance = (currencyAccount.getBalance()
                .multiply(
                        new BigDecimal(currencyAccount.getCurrency()
                                .equals(revaluationCurrency) ? 1.0000 : rateDTO.getExchangeRate()))
                .setScale(4, RoundingMode.HALF_UP));
        return (T) new BalanceDTO(account.getId(), currencyAccount.getBalance(),  revaluationBalance, rateDTO);
    }

    @Override
    public <T extends Account> T processRevaluationAccount(Account account, Currency revaluationCurrency, Map<String, RateDTO> currencyRates) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType {
        CurrencyAccount currencyAccount = (CurrencyAccount) account;
        BalanceDTO balanceDTO = this.processRevaluationBalance(currencyAccount, revaluationCurrency, currencyRates);
        currencyAccount.setRevaluationBalance(balanceDTO);
        return (T) currencyAccount;
    }

    @Override
    public String getType() {
        return AccountType.CURRENCY.name();
    }

}
