package com.bombulis.accounting.service.CurrencyService;

import com.bombulis.accounting.entity.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();

    Currency findCurrency(String currencyIsoCode) throws CurrencyNonFound;
}
