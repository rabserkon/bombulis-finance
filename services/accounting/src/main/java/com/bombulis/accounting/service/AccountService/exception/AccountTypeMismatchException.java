package com.bombulis.accounting.service.AccountService.exception;

import com.bombulis.accounting.service.AccountService.exception.AccountException;

public class AccountTypeMismatchException extends AccountException {
    public AccountTypeMismatchException(String message) {
        super(message);
    }
}
