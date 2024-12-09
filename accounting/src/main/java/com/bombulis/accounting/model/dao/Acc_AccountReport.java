package com.bombulis.accounting.model.dao;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acc_AccountReport {
    private String statementId;  // Идентификатор выписки
    private String accountNumber; // Номер счета
    private String accountHolderName; // Имя владельца счета
    private LocalDateTime statementPeriodStart; // Начало периода выписки
    private LocalDateTime statementPeriodEnd; // Конец периода выписки
    private Acc_Account account;
    private Acc_ConsolidateAccount consolidateAccount;
    private List<Acc_Transaction> transactions;
    private String documentType;


    public Acc_AccountReport(String documentType) {
        this.documentType = documentType;
    }
}
