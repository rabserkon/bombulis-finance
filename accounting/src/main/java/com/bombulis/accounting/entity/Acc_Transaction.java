package com.bombulis.accounting.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@NoArgsConstructor
@Entity
@Data
@ToString(onlyExplicitlyIncluded = true)
@Table(name = "transactionaccount")
@Getter@Setter
public class Acc_Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Getter
    @Setter
    private Long id;

    @Column
    private String operationId;

    @Getter @Setter
    private BigDecimal sendAmount;

    @Getter @Setter
    private String description;

    @Getter @Setter
    private String type;

    @Getter @Setter
    private Timestamp transactionDate;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "user_account_id", nullable = false)
    @JsonBackReference
    private Acc_User user;

    @ManyToOne
    @JoinColumn(name = "sender_account_id")
    private Acc_Account senderAccount;

    @ManyToOne
    @JoinColumn(name = "recipient_account_id")
    private Acc_Account recipientAccount;



    public Acc_Transaction(BigDecimal sendAmount, String description, String type, Acc_User user, Timestamp transactionDate) {
        this.operationId = "false";
        this.sendAmount = sendAmount;
        this.description = description;
        this.type = type;
        this.user = user;
        this.transactionDate = transactionDate;
    }
}
