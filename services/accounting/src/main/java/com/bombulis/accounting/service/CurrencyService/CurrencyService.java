package com.bombulis.accounting.service.CurrencyService;

import com.bombulis.accounting.entity.Currency;

import java.util.List;

public interface CurrencyService {
    List<Currency> getAllCurrencies();

    Currency getMainUserCurrency(Long userId) throws CurrencyNonFound;

    Currency findCurrency(String currencyIsoCode) throws CurrencyNonFound;
}
