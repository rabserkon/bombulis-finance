package com.bombulis.accounting.service.RevaluationService.RevaluationProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_RateDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.service.AccountService.Acc_AccountType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

@Service
public class Acc_CurrencyRevaluationProcessor implements Acc_RevaluationProcessor {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public <T extends Acc_BalanceDTO> T processRevaluationBalance(Acc_Account account, Acc_Currency revaluationCurrency, Map<String, Acc_RateDTO> currencyRates) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType {
        Acc_CurrencyAccount currencyAccount = (Acc_CurrencyAccount) account;
        Acc_RateDTO rateDTO = currencyRates.get(currencyAccount.getCurrency().getIsoCode());
        if (rateDTO == null && currencyAccount.getCurrency().equals(revaluationCurrency)){
            rateDTO = new Acc_RateDTO(
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
        return (T) new Acc_BalanceDTO(account.getId(), currencyAccount.getBalance(),  revaluationBalance, rateDTO);
    }

    @Override
    public <T extends Acc_Account> T processRevaluationAccount(Acc_Account account, Acc_Currency revaluationCurrency, Map<String, Acc_RateDTO> currencyRates) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType {
        Acc_CurrencyAccount currencyAccount = (Acc_CurrencyAccount) account;
        Acc_BalanceDTO balanceDTO = this.processRevaluationBalance(currencyAccount, revaluationCurrency, currencyRates);
        currencyAccount.setRevaluationBalance(balanceDTO);
        return (T) currencyAccount;
    }

    @Override
    public String getType() {
        return Acc_AccountType.CURRENCY.name();
    }

}
