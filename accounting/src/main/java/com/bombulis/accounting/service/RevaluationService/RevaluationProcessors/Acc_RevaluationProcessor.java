package com.bombulis.accounting.service.RevaluationService.RevaluationProcessors;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_RateDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;

import java.util.Map;

public interface Acc_RevaluationProcessor {
    <T extends Acc_BalanceDTO> T processRevaluationBalance(Acc_Account account, Acc_Currency revaluationCurrency, Map<String, Acc_RateDTO> currencyRates) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType;
    <T extends Acc_Account> T processRevaluationAccount(Acc_Account account, Acc_Currency revaluationCurrency, Map<String, Acc_RateDTO> currencyRates) throws Acc_AccountNonFound, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountOtherType;
    public String getType();

}
