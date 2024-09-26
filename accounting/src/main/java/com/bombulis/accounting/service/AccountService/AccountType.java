package com.bombulis.accounting.service.AccountService;

import lombok.Getter;
import lombok.Setter;

public enum AccountType {
    CURRENCY("Currency"),
    SECURITIES("Securities"),
    METALS("Metals"),
    CRYPTOCURRENCY("Cryptocurrency"),
    INVESTMENT("Investment"),
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal");

    @Getter
    private final String type;

    AccountType(String type) {
        this.type = type;
    }
}
