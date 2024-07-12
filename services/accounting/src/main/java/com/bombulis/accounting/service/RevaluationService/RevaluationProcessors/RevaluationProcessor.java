package com.bombulis.accounting.service.RevaluationService.RevaluationProcessors;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.dto.RateDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;

import java.util.Map;

public interface RevaluationProcessor {
    <T extends BalanceDTO> T processRevaluationBalance(Account account, Currency revaluationCurrency, Map<String, RateDTO> currencyRates) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType;
    <T extends Account> T processRevaluationAccount(Account account, Currency revaluationCurrency, Map<String, RateDTO> currencyRates) throws AccountNonFound, CurrencyMismatchException, AccountTypeMismatchException, AccountOtherType;
    public String getType();

}
