package com.bombulis.accounting.service.TransactionService;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import com.bombulis.accounting.dto.Acc_TransactionDTO;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountNonFound;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountOtherType;
import com.bombulis.accounting.service.AccountService.exception.Acc_AccountTypeMismatchException;
import com.bombulis.accounting.service.TransactionService.exception.Acc_CurrencyMismatchException;
import com.bombulis.accounting.service.UserService.Acc_UserException;

public interface Acc_TransactionBalanceService {

    Acc_TransactionAccount createTransaction(Acc_TransactionDTO transactionDTO, Long userId) throws Acc_AccountException, Acc_UserException;
    Acc_BalanceDTO balanceReconciliation(Long accountId, Long userId) throws Acc_AccountNonFound, Acc_AccountTypeMismatchException, Acc_AccountOtherType, Acc_CurrencyMismatchException;
    Acc_BalanceDTO calculateBalance(Long accountId, Long userId) throws Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound, Acc_AccountException;
    <T extends Acc_CurrencyAccount> T calculateBalance(T account, Long userId) throws Acc_CurrencyMismatchException, Acc_AccountTypeMismatchException, Acc_AccountNonFound;
}
