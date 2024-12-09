package com.bombulis.stock.control.service.OperationService;

public class TransactionExceptionAccountService extends Exception {
    public TransactionExceptionAccountService() {
    }

    public TransactionExceptionAccountService(String message) {
        super(message);
    }

    public TransactionExceptionAccountService(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionExceptionAccountService(Throwable cause) {
        super(cause);
    }

    public TransactionExceptionAccountService(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
