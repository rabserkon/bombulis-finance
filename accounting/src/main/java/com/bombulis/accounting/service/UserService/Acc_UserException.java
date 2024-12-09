package com.bombulis.accounting.service.UserService;

public class Acc_UserException extends Exception{
    public Acc_UserException() {
    }

    public Acc_UserException(String message) {
        super(message);
    }

    public Acc_UserException(String message, Throwable cause) {
        super(message, cause);
    }
}
