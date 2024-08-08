package com.bombulis.accounting.model.dao;

import java.util.List;

public class AccountReport {
    private String statementId;  // Идентификатор выписки
    private String accountNumber; // Номер счета
    private String accountHolderName; // Имя владельца счета
    private String statementPeriodStart; // Начало периода выписки
    private String statementPeriodEnd; // Конец периода выписки
    private Account account;
    private List<Transaction> transactions;

}
