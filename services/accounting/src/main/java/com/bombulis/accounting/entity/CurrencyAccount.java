package com.bombulis.accounting.entity;

import com.bombulis.accounting.model.AccountInformation;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
public class CurrencyAccount extends Assets  {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "currency_code", nullable = false, referencedColumnName = "isoCode")
    @Getter @Setter
    private Currency currency;

    @Column(name = "balance")
    @Getter @Setter
    private BigDecimal balance;


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
