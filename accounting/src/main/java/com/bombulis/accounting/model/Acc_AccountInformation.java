package com.bombulis.accounting.model;

import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class Acc_AccountInformation<T extends Acc_CurrencyAccount>  {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String number;
    @Getter @Setter
    private BigDecimal balance;
    @Getter @Setter
    private String currency_code;

    public Acc_AccountInformation(String name, String number, BigDecimal balance, String currency_code) {
        this.name = name;
        this.number = number;
        this.balance = balance;
        this.currency_code = currency_code;
    }

    public Acc_AccountInformation(T account) {
        this.name = account.getName();
        this.number = account.getId().toString();
        this.balance = new BigDecimal("0.00");
        this.currency_code = null;
    }
}
