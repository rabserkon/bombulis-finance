package com.bombulis.accounting.service.AccountService.exception;

public class AccountNonFound extends AccountException {
    public AccountNonFound(String message) {
        super(message);
    }
    public AccountNonFound() {
        super("Account not found");
    }
}
