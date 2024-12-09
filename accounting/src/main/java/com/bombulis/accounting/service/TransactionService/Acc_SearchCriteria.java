package com.bombulis.accounting.service.TransactionService;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class Acc_SearchCriteria {
    @Getter @Setter
    private String accountNumber;
    @Getter @Setter
    private LocalDate from;
    @Getter @Setter
    private LocalDate to;

    public Acc_SearchCriteria(LocalDate from, LocalDate to) {
        this.accountNumber = accountNumber;
        this.from = from;
        this.to = to;
    }
}
