package com.bombulis.accounting.service.RevaluationService;
import com.bombulis.accounting.dto.RateDTO;
import  com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;

import java.io.IOException;
import java.util.*;

public interface ExchangeRateService {
    Map<String, RateDTO> getExchangeRate(Currency mainCurrency, Collection<Currency> currencyCollection, Date date) throws ServerDataAssetsException;
    Map<String, RateDTO> getExchangeRate(Currency mainCurrency, Set<String> currencyCollection, Date date) throws ServerDataAssetsException;
    Map<String, RateDTO> getExchangeRate(List<Currency> mainCurrency, List<Currency> currencyCollection, Date date) throws ServerDataAssetsException;
}
