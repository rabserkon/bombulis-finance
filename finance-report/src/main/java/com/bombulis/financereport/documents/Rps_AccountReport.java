package com.bombulis.financereport.documents;

import com.bombulis.financereport.dto.Rps_Account;
import com.bombulis.financereport.dto.Rps_ConsolidateAccount;
import com.bombulis.financereport.dto.Rps_Transaction;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "account_oper_report")
public class Rps_AccountReport {
    @Id
    private String id;
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
