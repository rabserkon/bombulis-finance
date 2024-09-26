package com.bombulis.financereport.documents;

import com.bombulis.financereport.dto.Account;
import com.bombulis.financereport.dto.ConsolidateAccount;
import com.bombulis.financereport.dto.Transaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "account_oper_report")
public class AccountReport {
    @Id
    private String id;
    private String statementId;
    private String accountNumber;
    private String accountHolderName;
    private LocalDateTime statementPeriodStart;
    private LocalDateTime statementPeriodEnd;
    private Account account;
    private ConsolidateAccount consolidateAccount;
    private List<Transaction> transactions;
    private String documentType;
}
