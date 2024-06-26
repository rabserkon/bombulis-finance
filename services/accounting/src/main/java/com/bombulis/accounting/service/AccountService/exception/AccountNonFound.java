package com.bombulis.accounting.service.AccountService.exception;

import com.bombulis.accounting.service.AccountService.exception.AccountException;

public class AccountNonFound extends AccountException {
    public AccountNonFound(String message) {
        super(message);
    }
    public AccountNonFound() {
        super("Account not found");
    }
}
