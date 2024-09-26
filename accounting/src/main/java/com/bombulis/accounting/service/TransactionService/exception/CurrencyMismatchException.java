package com.bombulis.accounting.service.TransactionService.exception;

import com.bombulis.accounting.service.AccountService.exception.AccountException;

public class CurrencyMismatchException extends AccountException {
    public CurrencyMismatchException(String message) {
        super(message);
    }

    public CurrencyMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
