package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Acc_TransactionAccount;
import com.bombulis.accounting.entity.Acc_TransactionCurrencyExchange;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Acc_ResponseTransaction {
    private Long id;
    private BigDecimal sendAmount;
    private String description;
    private Acc_ResponseAccountDTO senderAccount;
    private Acc_ResponseAccountDTO recipientAccount;
    private Timestamp transactionDate;
    private String type;
    private Long userId;
    private BigDecimal receivedAmount;
    private BigDecimal exchangeRate;

    public Acc_ResponseTransaction(Acc_TransactionAccount transaction) {
        this.id = transaction.getId();
        this.sendAmount = transaction.getSendAmount();
        this.description = transaction.getDescription();
        this.senderAccount = new Acc_ResponseAccountDTO(transaction.getSenderAccount());
        this.recipientAccount = new Acc_ResponseAccountDTO(transaction.getRecipientAccount());
        this.transactionDate = transaction.getTransactionDate();
        this.type = transaction.getType();
        this.userId = transaction.getUser().getUserId();
        if (transaction instanceof Acc_TransactionCurrencyExchange){
            Acc_TransactionCurrencyExchange currencyExchange = ((Acc_TransactionCurrencyExchange) transaction);
            this.receivedAmount = currencyExchange.getReceivedAmount();
            this.exchangeRate = currencyExchange.getExchangeRate();
        }

    }
}
