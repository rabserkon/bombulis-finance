package com.bombulis.accounting.service.RevaluationService;

import com.bombulis.accounting.dto.Acc_RateDTO;
import com.bombulis.accounting.entity.Acc_Currency;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.google.gson.JsonObject;
import com.bombulis.accounting.service.NasdaqApi.YahooClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class Acc_YahooExchangeRateService implements Acc_ExchangeRateService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Map<String, Acc_RateDTO>  getExchangeRate(Acc_Currency mainCurrency, Collection<Acc_Currency> currencyCollection, Date date) throws Acc_CurrencyRateException, Acc_ServerDataAssetsException {
        YahooClient yahooClient = new YahooClient();
        List<String> rates = currencyCollection.stream().map(i -> {
            return i.getIsoCode() + mainCurrency.getIsoCode() +"=X";
        }).collect(Collectors.toList());
        Map<String, JsonObject> currencyWithRates = null;
        try {
            currencyWithRates = yahooClient.getTickersData(rates);
            Map<String, Acc_RateDTO> convertedMap = currencyWithRates.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> {
                            String symbol = entry.getValue().get("symbol").getAsString();
                            return symbol.substring(0,symbol.indexOf(mainCurrency.getIsoCode() + "=X"));
                        },
                        entry -> {
                            String symbol = entry.getValue().get("symbol").getAsString();
                            return Acc_RateDTO.builder()
                                    .fullName(entry.getValue().get("longName").getAsString())
                                    .exchangeRate(entry.getValue().get("price").getAsDouble())
                                    .mainCurrency(mainCurrency.getIsoCode())
                                    .ticker(symbol.substring(0,symbol.indexOf(mainCurrency.getIsoCode() + "=X")))
                                    .build();
                        }
                ));
            return convertedMap;
        } catch (IOException e) {;
            throw new Acc_CurrencyRateException("Ошибка загрузки курсов");
        }
    }

    @Override
    public Map<String, Acc_RateDTO> getExchangeRate(Acc_Currency mainCurrency, Set<String> currencyCollection, Date date) throws Acc_ServerDataAssetsException {
        return null;
    }

    @Override
    public Map<String, Acc_RateDTO> getExchangeRate(List<Acc_Currency> mainCurrency, List<Acc_Currency> currencyCollection, Date date) throws Acc_ServerDataAssetsException {
        Iterator<Acc_Currency> var1 = mainCurrency.iterator();
        List<String> ratesFull = new ArrayList<>();
        YahooClient yahooClient = new YahooClient();
        for (Acc_Currency main : mainCurrency) {
            for (Acc_Currency currency : currencyCollection) {
                if (!main.getIsoCode().equals(currency.getIsoCode())) {
                    ratesFull.add(main.getIsoCode() + currency.getIsoCode() + "=X");
                }
            }
        }
        try {
            Map<String, JsonObject> currencyWithRates = yahooClient.getTickersData(ratesFull);
            Map<String, Acc_RateDTO> convertedMap = currencyWithRates.entrySet().stream()
                    .collect(Collectors.toMap(
                            entry -> {
                                String symbol = entry.getValue().get("symbol").getAsString();
                                return symbol.substring(0,symbol.indexOf("=X"));
                            },
                            entry -> {
                                String symbol = entry.getValue().get("symbol").getAsString();
                                return Acc_RateDTO.builder()
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
