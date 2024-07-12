package com.bombulis.accounting.entity;

import com.bombulis.accounting.service.TransactionService.TransactionProcessors.TransactionType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

@NoArgsConstructor
@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
public class TransactionAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, columnDefinition = "serial")
    @Getter @Setter
    private Long id;

    @Getter @Setter
    private BigDecimal sendAmount;

    @Getter @Setter
    private String description;

    @ManyToOne
    @JoinColumn(name = "sender_account_id", nullable = false)
    private Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id", nullable = false)
    private Account recipientAccount;

    @Getter @Setter
    private Timestamp transactionDate;

    @Getter @Setter
    private String type;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    @JsonBackReference
    private User user;

    public TransactionAccount (String description, BigDecimal amount, Account senderAccount, Account recipientAccount, Date transactionDate, User user, String type) {
        this.description = description;
        this.sendAmount = amount;
        this.senderAccount = senderAccount;
        this.recipientAccount = recipientAccount;
        this.transactionDate = new Timestamp(transactionDate.getTime());
        this.type = type;
        this.user = user;
    }
}
