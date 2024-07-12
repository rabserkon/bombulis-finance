package com.bombulis.accounting.service.RevaluationService;

public class CurrencyRateException extends Exception{
    public CurrencyRateException() {
    }

    public CurrencyRateException(String message) {
        super(message);
    }

    public CurrencyRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
