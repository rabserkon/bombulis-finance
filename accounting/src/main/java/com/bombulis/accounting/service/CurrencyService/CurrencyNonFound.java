package com.bombulis.accounting.service.CurrencyService;

public class CurrencyNonFound extends CurrencyException {
    public CurrencyNonFound(String message) {
        super(message);
    }
    public CurrencyNonFound() {
        super("Currency not found");
    }
}
