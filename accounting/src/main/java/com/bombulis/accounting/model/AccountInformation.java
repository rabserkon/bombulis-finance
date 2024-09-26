package com.bombulis.accounting.model;

import com.bombulis.accounting.entity.CurrencyAccount;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

public class AccountInformation<T extends CurrencyAccount>  {
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String number;
    @Getter @Setter
    private BigDecimal balance;
    @Getter @Setter
    private String currency_code;

    public AccountInformation(String name, String number, BigDecimal balance, String currency_code) {
        this.name = name;
        this.number = number;
        this.balance = balance;
        this.currency_code = currency_code;
    }

    public AccountInformation(T account) {
        this.name = account.getName();
        this.number = account.getId().toString();
        this.balance = new BigDecimal("0.00");
        this.currency_code = null;
    }
}
