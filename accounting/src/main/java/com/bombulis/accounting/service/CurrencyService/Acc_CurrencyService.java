package com.bombulis.accounting.service.CurrencyService;

import com.bombulis.accounting.entity.Acc_Currency;

import java.util.List;

public interface Acc_CurrencyService {
    List<Acc_Currency> getAllCurrencies();

    Acc_Currency getMainUserCurrency(Long userId) throws Acc_CurrencyNonFound;

    Acc_Currency findCurrency(String currencyIsoCode) throws Acc_CurrencyNonFound;
}
