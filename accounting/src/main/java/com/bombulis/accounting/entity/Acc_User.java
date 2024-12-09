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
public class Acc_User {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Timestamp createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_code", nullable = true, referencedColumnName = "isoCode")
    private Acc_Currency currency;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Acc_Account> accountList;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Acc_Transaction> transactionList;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<Acc_Role> roles;

    public Acc_User(Long userId) {
        this.userId = userId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.currency = null;
    }

    public Acc_User(Long userId, Acc_Currency currency) {
        this.userId = userId;
        this.createdAt = new Timestamp(System.currentTimeMillis());
        this.currency = currency;
    }
}
