package com.bombulis.accounting.service.RevaluationService;
import com.bombulis.accounting.dto.Acc_RateDTO;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;

import java.util.*;

public interface Acc_ExchangeRateService {
    Map<String, Acc_RateDTO> getExchangeRate(Acc_Currency mainCurrency, Collection<Acc_Currency> currencyCollection, Date date) throws Acc_ServerDataAssetsException, Acc_CurrencyRateException;
    Map<String, Acc_RateDTO> getExchangeRate(Acc_Currency mainCurrency, Set<String> currencyCollection, Date date) throws Acc_ServerDataAssetsException;
    Map<String, Acc_RateDTO> getExchangeRate(List<Acc_Currency> mainCurrency, List<Acc_Currency> currencyCollection, Date date) throws Acc_ServerDataAssetsException;
}
