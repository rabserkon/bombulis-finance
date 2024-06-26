package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.BalanceDTO;
import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.exception.AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.AccountTypeMismatchException;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.TransactionService.exception.CurrencyMismatchException;

import java.util.Date;
import java.util.List;

public interface RevaluationAccountService {
    <T extends BalanceDTO> List<T> getRevaluationAccountList(List<Account> accountList, Currency revaluationCurrency, Date date) throws AccountOtherType, CurrencyMismatchException, AccountTypeMismatchException, AccountNonFound, ServerDataAssetsException;

}
