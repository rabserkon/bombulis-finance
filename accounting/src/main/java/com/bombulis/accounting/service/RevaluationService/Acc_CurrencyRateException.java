package com.bombulis.accounting.service.RevaluationService;

public class Acc_CurrencyRateException extends Exception{
    public Acc_CurrencyRateException() {
    }

    public Acc_CurrencyRateException(String message) {
        super(message);
    }

    public Acc_CurrencyRateException(String message, Throwable cause) {
        super(message, cause);
    }
}
