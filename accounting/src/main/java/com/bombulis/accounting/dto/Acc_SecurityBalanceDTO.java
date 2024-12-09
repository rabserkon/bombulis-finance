package com.bombulis.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class Acc_SecurityBalanceDTO extends Acc_BalanceDTO {
    @Getter @Setter
    int quantity;

    public Acc_SecurityBalanceDTO(Long accountId,
                                  BigDecimal balance,
                                  BigDecimal revaluationBalance,
                                  Acc_RateDTO currency,
                                  int quantity) {
        super(accountId, balance, revaluationBalance, currency);
        this.quantity = quantity;
    }
}
