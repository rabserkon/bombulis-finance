package com.bombulis.stock.control.service.OperationService;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter @Setter
public class St_CurrencyResponse {
    private String currencyCode;
    private String currencyName;
    private BigDecimal exchangeRate;
}
