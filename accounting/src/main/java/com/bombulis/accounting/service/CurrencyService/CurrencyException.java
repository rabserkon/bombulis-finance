package com.bombulis.accounting.service.CurrencyService;

public class CurrencyException extends Exception {
    public CurrencyException() {
    }

    public CurrencyException(String message) {
        super(message);
    }

    public CurrencyException(String message, Throwable cause) {
        super(message, cause);
    }
}
