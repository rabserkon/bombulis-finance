package com.bombulis.accounting.service.AccountService.exception;

import com.bombulis.accounting.service.AccountService.exception.AccountException;

public class AccountOtherType extends AccountException {
    public AccountOtherType(String message) {
        super(message);
    }

    public AccountOtherType(String message, Throwable cause) {
        super(message, cause);
    }
}
