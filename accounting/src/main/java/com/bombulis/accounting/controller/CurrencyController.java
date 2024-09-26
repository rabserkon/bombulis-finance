package com.bombulis.accounting.controller;

import com.bombulis.accounting.dto.CurrencyDTO;
import com.bombulis.accounting.service.AccountService.exception.ServerDataAssetsException;
import com.bombulis.accounting.service.CurrencyService.CurrencyNonFound;
import com.bombulis.accounting.service.CurrencyService.CurrencyService;
import com.bombulis.accounting.service.RevaluationService.CurrencyRateException;
import com.bombulis.accounting.service.RevaluationService.ExchangeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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
public class CurrencyController {

    private CurrencyService currencyService;
    private ExchangeRateService rateService;

    @Secured("ROLE_USER")
    @RequestMapping(value = "/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrencies(){
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", currencyService.getAllCurrencies().stream()
                .map(i -> new CurrencyDTO(i)).collect(Collectors.toList()));
        return ResponseEntity.ok(response);
    }

    @Secured("ROLE_RATE")
    @RequestMapping(value = "/rates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCurrenciesRates(@RequestParam(name = "currency") String currency,
                                                Authentication authentication) throws CurrencyNonFound, ServerDataAssetsException, CurrencyRateException {
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", rateService.getExchangeRate(
                currencyService.findCurrency(currency),
                currencyService.getAllCurrencies(),
                new Date()
                ));
        return ResponseEntity.ok(response);
    }

    @Secured("ROLE_RATE")
    @RequestMapping(value = "/full/rates", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getFullCurrenciesRates(Authentication authentication) throws CurrencyNonFound, ServerDataAssetsException, IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", rateService.getExchangeRate(
                currencyService.getAllCurrencies(),
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
