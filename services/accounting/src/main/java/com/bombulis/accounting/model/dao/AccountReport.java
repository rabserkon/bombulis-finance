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
public class AccountReport {
    private String statementId;  // Идентификатор выписки
    private String accountNumber; // Номер счета
    private String accountHolderName; // Имя владельца счета
    private LocalDateTime statementPeriodStart; // Начало периода выписки
    private LocalDateTime statementPeriodEnd; // Конец периода выписки
    private Account account;
    private ConsolidateAccount consolidateAccount;
    private List<Transaction> transactions;

}
