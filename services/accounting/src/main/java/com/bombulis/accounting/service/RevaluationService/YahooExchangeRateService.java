package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.RateDTO;
import com.bombulis.accounting.entity.Currency;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.google.gson.JsonObject;
import com.bombulis.accounting.service.NasdaqApi.YahooClient;
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
    public Map<String, RateDTO>  getExchangeRate(Currency mainCurrency, Collection<Currency> currencyCollection, Date date) throws CurrencyRateException {
        YahooClient yahooClient = new YahooClient();
        List<String> rates = currencyCollection.stream().map(i -> {
            return i.getIsoCode() + mainCurrency.getIsoCode() +"=X";
        }).collect(Collectors.toList());
        Map<String, JsonObject> currencyWithRates = null;
        try {
            currencyWithRates = yahooClient.getTickersData(rates);
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
        } catch (IOException e) {;
            throw new CurrencyRateException("Ошибка загрузки курсов");
        }
    }

    @Override
    public Map<String, RateDTO> getExchangeRate(Currency mainCurrency, Set<String> currencyCollection, Date date) throws ServerDataAssetsException {
        return null;
    }

    @Override
    public Map<String, RateDTO> getExchangeRate(List<Currency> mainCurrency, List<Currency> currencyCollection, Date date) throws ServerDataAssetsException {
        Iterator<Currency> var1 = mainCurrency.iterator();
        List<String> ratesFull = new ArrayList<>();
        YahooClient yahooClient = new YahooClient();
        for (Currency main : mainCurrency) {
            for (Currency currency : currencyCollection) {
                if (!main.getIsoCode().equals(currency.getIsoCode())) {
                    ratesFull.add(main.getIsoCode() + currency.getIsoCode() + "=X");
                }
            }
        }
        try {
            Map<String, JsonObject> currencyWithRates = yahooClient.getTickersData(ratesFull);
            Map<String, RateDTO> convertedMap = currencyWithRates.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> {
                                String symbol = entry.getValue().get("symbol").getAsString();
                                return symbol.substring(0,symbol.indexOf("=X"));
                            },
                            entry -> {
                                String symbol = entry.getValue().get("symbol").getAsString();
                                return RateDTO.builder()
                                        .fullName(entry.getValue().get("longName").getAsString())
                                        .exchangeRate(entry.getValue().get("price").getAsDouble())
                                        .mainCurrency(symbol.substring(3,6))
                                        .ticker(symbol.substring(0,6))
                                        .build();
                            }
                    ));
            return convertedMap;
        } catch (IOException | StringIndexOutOfBoundsException e) {
            return new HashMap<>();
        }
    }
}
