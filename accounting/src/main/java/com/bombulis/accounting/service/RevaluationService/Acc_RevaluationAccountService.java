package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;

import java.util.Date;
import java.util.List;

public interface Acc_RevaluationAccountService {
    <T extends Acc_BalanceDTO> List<T> getRevaluationBalanceAccountList(List<Acc_Account> accountList, Acc_Currency revaluationCurrency, Date date) throws Acc_AccountOtherType, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_ServerDataAssetsException, Acc_CurrencyRateException;

    List<Acc_Account> getRevaluationAccountList(List<Acc_Account> accountList, String currencyISO, Date date) throws Acc_AccountOtherType, Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_ServerDataAssetsException, Acc_CurrencyNonFound, Acc_CurrencyRateException;
}
