package com.bombulis.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class SecurityBalanceDTO extends BalanceDTO{
    @Getter @Setter
    int quantity;

    public SecurityBalanceDTO(Long accountId,
                              BigDecimal balance,
                              BigDecimal revaluationBalance,
                              RateDTO currency,
                              int quantity) {
        super(accountId, balance, revaluationBalance, currency);
        this.quantity = quantity;
    }
}
