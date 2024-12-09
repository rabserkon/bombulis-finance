package com.bombulis.stock.control.controller;

import com.bombulis.stock.control.entity.St_Currency;
import com.bombulis.stock.control.service.OperationService.St_CurrencyService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class St_MainController {

    @Autowired
    private St_CurrencyService currencyService;

    @GetMapping("/get/available/currency")
    public Map<String,Object> availableCurrency(HttpServletResponse response) throws IOException {
        Map<String,Object> object = new HashMap<>();
        St_Currency currency = currencyService.findCurrency("USD");
        object.put("status", "success");
        object.put("param", "USD");
        object.put("currency", currency.toString());
        return object;
    }
}
