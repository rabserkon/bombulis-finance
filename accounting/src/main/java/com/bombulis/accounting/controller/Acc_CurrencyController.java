package com.bombulis.accounting.controller;

import com.bombulis.accounting.dto.Acc_CurrencyDTO;
import com.bombulis.accounting.service.AccountService.exception.Acc_ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.Acc_CurrencyService;
import com.bombulis.accounting.service.RevaluationService.Acc_CurrencyRateException;
import com.bombulis.accounting.service.RevaluationService.Acc_ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/v1/currencies")
public class Acc_CurrencyController {

    private Acc_CurrencyService currencyService;
    private Acc_ExchangeRateService rateService;

    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrencies(){
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", currencyService.getAllCurrencies().stream()
                .map(i -> new Acc_CurrencyDTO(i)).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/rates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrenciesRates(@RequestParam(name = "currency") String currency,
                                                Authentication authentication) throws Acc_CurrencyNonFound, Acc_ServerDataAssetsException, Acc_CurrencyRateException {
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", rateService.getExchangeRate(
                currencyService.findCurrencyByIsoCode(currency),
                currencyService.getAllCurrencies(),
                new Date()
                ));
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "/full/rates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFullCurrenciesRates(Authentication authentication) throws Acc_CurrencyNonFound, Acc_ServerDataAssetsException, IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", rateService.getExchangeRate(
                currencyService.getAllCurrencies(),
                currencyService.getAllCurrencies(),
                new Date()
        ));
        return ResponseEntity.ok(response);
    }

    @Autowired
    public void setRateService(Acc_ExchangeRateService rateService) {
        this.rateService = rateService;
    }

    @Autowired
    public void setCurrencyService(Acc_CurrencyService currencyService) {
        this.currencyService = currencyService;
    }
}
