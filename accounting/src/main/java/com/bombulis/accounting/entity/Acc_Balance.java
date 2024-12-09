package com.bombulis.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class Acc_Balance {
    //ISO code
    private String mainCurrency;
    private BigDecimal balanceInMainISOCurrency;
    //ISO Code
    private String revaluationCurrency;
    private BigDecimal balanceInRevaluationISOCurrency;
}
