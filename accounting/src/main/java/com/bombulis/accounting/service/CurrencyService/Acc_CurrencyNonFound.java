package com.bombulis.accounting.service.CurrencyService;

public class Acc_CurrencyNonFound extends Acc_CurrencyException {
    public Acc_CurrencyNonFound(String message) {
        super(message);
    }
    public Acc_CurrencyNonFound() {
        super("Currency not found");
    }
}
