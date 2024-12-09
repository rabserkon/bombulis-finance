package com.bombulis.stock.control.service.AccountService;

public class St_AccountBalanceException extends St_AccountException{
    public St_AccountBalanceException(String message) {
        super(message);
    }

    public St_AccountBalanceException(String message, Throwable cause) {
        super(message, cause);
    }
}
