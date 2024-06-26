package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.RateDTO;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.google.gson.JsonObject;
import org.nasduq.api.YahooClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class YahooExchangeRateService implements ExchangeRateService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, RateDTO>  getExchangeRate(Currency mainCurrency, Collection<Currency> currencyCollection, Date date) throws ServerDataAssetsException {
        YahooClient yahooClient = new YahooClient();
        List<String> rates = currencyCollection.stream().map(i -> {
            return i.getIsoCode() + mainCurrency.getIsoCode() +"=X";
        }).collect(Collectors.toList());
        Map<String, JsonObject> currencyWithRates = null;
        try {
            currencyWithRates = yahooClient.getTickersData(rates);
        } catch (IOException e) {
            throw new ServerDataAssetsException(e.getMessage());
        }
        Map<String, RateDTO> convertedMap = currencyWithRates.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> {
                            String symbol = entry.getValue().get("symbol").getAsString();
                            return symbol.substring(0,symbol.indexOf(mainCurrency.getIsoCode() + "=X"));
                        },
                        entry -> {
                            String symbol = entry.getValue().get("symbol").getAsString();
                            return RateDTO.builder()
                                    .fullName(entry.getValue().get("longName").getAsString())
                                    .exchangeRate(entry.getValue().get("price").getAsDouble())
                                    .mainCurrency(mainCurrency.getIsoCode())
                                    .ticker(symbol.substring(0,symbol.indexOf(mainCurrency.getIsoCode() + "=X")))
                                    .build();
                        }
                ));
        return convertedMap;
    }

    @Override
    public Map<String, RateDTO> getExchangeRate(Currency mainCurrency, Set<String> currencyCollection, Date date) throws ServerDataAssetsException {
        return null;
    }
}
