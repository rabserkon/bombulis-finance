package com.bombulis.accounting.dto;

import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionType;
import lombok.Getter;
import lombok.Setter;

public class SecurityTransactionDTO extends TransactionDTO {
    @Getter @Setter
    private String ticker;
    @Getter @Setter
    private TransactionType transactionType;
    @Getter @Setter
    private int quantity;
    @Getter @Setter
    private float securityPrice;
}
