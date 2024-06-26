package com.bombulis.accounting.entity;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounting_user")
public class User {

    @Id
    @Column(name = "user_id")
    @Getter @Setter
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_code", nullable = true, referencedColumnName = "isoCode")
    @Getter
    private Currency currency;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Account> accountList;

    @OneToMany(mappedBy = "user")
    private List<TransactionAccount> transactionList;

    public User(Long userId) {
        this.userId = userId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.currency = null;
    }

    public User(Long userId, Currency currency) {
        this.userId = userId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.currency = currency;
    }
}