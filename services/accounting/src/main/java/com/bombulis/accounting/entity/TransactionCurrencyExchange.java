package com.bombulis.accounting.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Entity
public class TransactionCurrencyExchange extends TransactionAccount {

    @Getter @Setter
    private BigDecimal receivedAmount; // Полученная сумма

    @Getter @Setter
    private BigDecimal exchangeRate; // Курс обмена

    public TransactionCurrencyExchange(String description,
                                       BigDecimal sendAmount,
                                       BigDecimal receivedAmount,
                                       BigDecimal exchangeRate,
                                       Account senderAccount,
                                       Account recipientAccount,
                                       Date transactionDate,
                                       User user,
                                       String type) {
        super(description, sendAmount, senderAccount, recipientAccount, new Timestamp(transactionDate.getTime()), user, type);
        this.receivedAmount = receivedAmount;
        this.exchangeRate = exchangeRate;
    }
}
