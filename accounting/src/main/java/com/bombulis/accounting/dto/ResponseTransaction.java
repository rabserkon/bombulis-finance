package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.TransactionAccount;
import com.bombulis.accounting.entity.TransactionCurrencyExchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseTransaction {
    private Long id;
    private BigDecimal sendAmount;
    private String description;
    private ResponseAccountDTO senderAccount;
    private ResponseAccountDTO recipientAccount;
    private Timestamp transactionDate;
    private String type;
    private Long userId;
    private BigDecimal receivedAmount;
    private BigDecimal exchangeRate;

    public ResponseTransaction(TransactionAccount transaction) {
        this.id = transaction.getId();
        this.sendAmount = transaction.getSendAmount();
        this.description = transaction.getDescription();
        this.senderAccount = new ResponseAccountDTO(transaction.getSenderAccount());
        this.recipientAccount = new ResponseAccountDTO(transaction.getRecipientAccount());
        this.transactionDate = transaction.getTransactionDate();
        this.type = transaction.getType();
        this.userId = transaction.getUser().getUserId();
        if (transaction instanceof TransactionCurrencyExchange){
            TransactionCurrencyExchange currencyExchange = ((TransactionCurrencyExchange) transaction);
            this.receivedAmount = currencyExchange.getReceivedAmount();
            this.exchangeRate = currencyExchange.getExchangeRate();
        }

    }
}
