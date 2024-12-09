package com.bombulis.accounting.entity;

import com.bombulis.accounting.service.TransactionService.TransactionProcessors.Acc_TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Entity
@Getter @Setter
public class Acc_TransactionAccount extends Acc_Transaction{



    public Acc_TransactionAccount(String description, BigDecimal amount, Acc_Account senderAccount, Acc_Account recipientAccount, Date transactionDate, Acc_User user, String type) {
            super(amount, description, type, user, new Timestamp(transactionDate.getTime()));
    }



}
