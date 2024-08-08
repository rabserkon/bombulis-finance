package com.bombulis.financereport.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AccountReport {
    private String statementId;
    private String accountNumber;
    private String accountHolderName;
    private String statementPeriodStart;
    private String statementPeriodEnd;
    private Account account;
    private ConsolidateAccount consolidateAccount;
    private List<Transaction> transactions;
}
