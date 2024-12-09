package com.bombulis.stock.control.service.AccountService;

public class St_AccountNotFoundException extends St_AccountException{


    public St_AccountNotFoundException(String message) {
        super(message);
    }

    public St_AccountNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
