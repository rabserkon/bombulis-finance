package com.bombulis.accounting.entity;

import com.bombulis.accounting.dto.BalanceDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Getter @Setter
public class CurrencyAccount extends Assets  {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_code", nullable = false, referencedColumnName = "isoCode")
    private Currency currency;

    @Column(name = "balance")
    private BigDecimal balance;

    @Transient
    private BalanceDTO revaluationBalance;

    private String subAccount;

    public CurrencyAccount(String name, User user) {
        super(name, user);
        this.balance = new BigDecimal("0.00");
    }

    public CurrencyAccount(String name, Currency currency, User user) {
        super(name, user);
        this.currency = currency;
        this.balance = new BigDecimal("0.00");
    }



}