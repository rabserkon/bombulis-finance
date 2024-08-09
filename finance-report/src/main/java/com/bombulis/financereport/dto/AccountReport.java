package com.bombulis.financereport.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class AccountReport {
    private String statementId;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime statementPeriodStart;
    private LocalDateTime statementPeriodEnd;
    private Account account;
    private ConsolidateAccount consolidateAccount;
    private List<Transaction> transactions;
}
