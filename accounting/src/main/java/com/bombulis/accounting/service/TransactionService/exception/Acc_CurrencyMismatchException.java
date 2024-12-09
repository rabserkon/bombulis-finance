package com.bombulis.accounting.service.TransactionService.exception;

import com.bombulis.accounting.service.AccountService.exception.Acc_AccountException;

public class Acc_CurrencyMismatchException extends Acc_AccountException {
    public Acc_CurrencyMismatchException(String message) {
        super(message);
    }

    public Acc_CurrencyMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
