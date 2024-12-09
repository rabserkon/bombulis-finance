package com.bombulis.accounting.service.AccountService;

import lombok.Getter;

public enum Acc_AccountType {
    CURRENCY("Currency"),
    SECURITIES("Securities"),
    METALS("Metals"),
    CRYPTOCURRENCY("Cryptocurrency"),
    INVESTMENT("Investment"),
    DEPOSIT("Deposit"),
    WITHDRAWAL("Withdrawal"),
    BROKERCURRENCY("Broker-Currency");

    @Getter
    private final String type;

    Acc_AccountType(String type) {
        this.type = type;
    }
}
