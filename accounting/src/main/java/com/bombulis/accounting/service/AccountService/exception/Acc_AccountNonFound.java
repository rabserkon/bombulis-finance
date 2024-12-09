package com.bombulis.accounting.service.AccountService.exception;

public class Acc_AccountNonFound extends Acc_AccountException {
    public Acc_AccountNonFound(String message) {
        super(message);
    }
    public Acc_AccountNonFound() {
        super("Account not found");
    }
}
