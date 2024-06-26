package com.bombulis.accounting.service.TransactionService;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class SearchCriteria {
    @Getter @Setter
    private String accountNumber;
    @Getter @Setter
    private LocalDate from;
    @Getter @Setter
    private LocalDate to;

    public SearchCriteria( LocalDate from, LocalDate to) {
        this.accountNumber = accountNumber;
        this.from = from;
        this.to = to;
    }
}
