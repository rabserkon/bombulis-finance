package com.bombulis.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
public class Acc_BalanceDTO {
    @Getter @Setter
    Long accountId;
    @Getter @Setter
    BigDecimal balance;
    @Getter @Setter
    BigDecimal revaluationBalance;
    @Getter @Setter
    Acc_RateDTO currency;

    public Acc_BalanceDTO(Long accountId, BigDecimal balance) {
        this.accountId = accountId;
        this.balance = balance;
    }

}
