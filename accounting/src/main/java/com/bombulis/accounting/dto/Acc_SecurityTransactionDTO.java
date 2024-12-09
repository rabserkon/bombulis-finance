package com.bombulis.accounting.dto;

import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionType;
import lombok.Getter;
import lombok.Setter;

public class Acc_SecurityTransactionDTO extends Acc_TransactionDTO {
    @Getter @Setter
    private String ticker;
    @Getter @Setter
    private Acc_TransactionType transactionType;
    @Getter @Setter
    private int quantity;
    @Getter @Setter
    private float securityPrice;
}
