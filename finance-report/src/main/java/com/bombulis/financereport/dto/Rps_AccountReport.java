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
public class Rps_AccountReport {
    private String statementId;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime statementPeriodStart;
    private LocalDateTime statementPeriodEnd;
    private Rps_Account account;
    private Rps_ConsolidateAccount consolidateAccount;
    private List<Rps_Transaction> transactions;
    private String documentType;
}
