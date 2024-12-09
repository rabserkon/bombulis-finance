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
@Getter @Setter
public class Acc_TransactionCurrencyExchange extends Acc_TransactionAccount {

    private BigDecimal receivedAmount;

    private BigDecimal exchangeRate;

    public Acc_TransactionCurrencyExchange(String description,
                                           BigDecimal sendAmount,
                                           BigDecimal receivedAmount,
                                           BigDecimal exchangeRate,
                                           Acc_Account senderAccount,
                                           Acc_Account recipientAccount,
                                           Date transactionDate,
                                           Acc_User user,
                                           String type) {
        super(description, sendAmount, senderAccount, recipientAccount, new Timestamp(transactionDate.getTime()), user, type);
        this.receivedAmount = receivedAmount;
        this.exchangeRate = exchangeRate;
    }

}
