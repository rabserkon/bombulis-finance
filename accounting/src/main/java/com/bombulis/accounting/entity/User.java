package com.bombulis.accounting.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "accounting_user")
@Getter @Setter
public class User {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code", nullable = true, referencedColumnName = "isoCode")
    private Currency currency;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Account> accountList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<TransactionAccount> transactionList;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Role> roles;

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
