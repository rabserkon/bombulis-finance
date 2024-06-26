package com.bombulis.accounting.controller;

import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.CurrencyService;
import com.bombulis.accounting.service.RevaluationService.ExchangeRateService;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/currencies")
public class CurrencyController {

    private CurrencyService currencyService;
    private ExchangeRateService rateService;

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrencies(Authentication authentication){
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", currencyService.getAllCurrencies());
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/rates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrenciesRates(@RequestParam(name = "currency") String currency,
                                                Authentication authentication) throws CurrencyNonFound, ServerDataAssetsException {
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", rateService.getExchangeRate(
                currencyService.findCurrency(currency),
                currencyService.getAllCurrencies(),
                new Date()
                ));
        return ResponseEntity.ok(response);
    }


    @Autowired
    public void setRateService(ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
}
