package com.bombulis.accounting.service.AccountService.exception;

public class Acc_ServerDataAssetsException extends Exception {
    public Acc_ServerDataAssetsException() {
    }

    public Acc_ServerDataAssetsException(String message) {
        super(message);
    }

    public Acc_ServerDataAssetsException(String message, Throwable cause) {
        super(message, cause);
    }

    public Acc_ServerDataAssetsException(Throwable cause) {
        super(cause);
    }

    public Acc_ServerDataAssetsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
