package com.bombulis.accounting.entity;

import com.bombulis.accounting.dto.Acc_BalanceDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter @Setter
public class Acc_CurrencyAccount extends Acc_Assets {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_code", nullable = false, referencedColumnName = "isoCode")
    private Acc_Currency currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Transient
    private Acc_BalanceDTO revaluationBalance;

    private String subAccount;


    public Acc_CurrencyAccount(String name, Acc_User user) {
        super(name, user);
        this.balance = new BigDecimal("0.00");
    }

    public Acc_CurrencyAccount(String name, Acc_Currency currency, Acc_User user) {
        super(name, user);
        this.currency = currency;
        this.balance = new BigDecimal("0.00");
    }





}
