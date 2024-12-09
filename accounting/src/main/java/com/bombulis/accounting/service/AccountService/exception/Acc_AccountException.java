package com.bombulis.accounting.service.AccountService.exception;

public class Acc_AccountException extends Exception{

    public Acc_AccountException(String message) {
        super(message);
    }

    public Acc_AccountException(String message, Throwable cause) {
        super(message, cause);
    }
}
