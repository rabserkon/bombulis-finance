package com.bombulis.accounting.service.AccountService.exception;

public class ServerDataAssetsException extends Exception {
    public ServerDataAssetsException() {
    }

    public ServerDataAssetsException(String message) {
        super(message);
    }

    public ServerDataAssetsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServerDataAssetsException(Throwable cause) {
        super(cause);
    }

    public ServerDataAssetsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
