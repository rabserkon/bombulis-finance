package com.bombulis.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Acc_SecurityPositionAccount extends Acc_Assets {

    @Column(name = "balance")
    @Getter @Setter
    private BigDecimal balance;
    @Getter @Setter
    private String ticker;
    @Getter @Setter
    private int quantity;
    @Getter @Setter
    private double purchasePrice;
    @ManyToOne
    @JoinColumn(name = "security_currency", referencedColumnName="isoCode")
    @Getter @Setter
    private Acc_Currency currency;


    public Acc_SecurityPositionAccount(String name,
                                       Acc_User user,
                                       String ticker,
                                       Acc_Currency currency) {
        super(name, user);
        this.balance = new BigDecimal("0.00");
        this.ticker = ticker;
        this.quantity = 0;
        this.purchasePrice = 0;
        this.currency = currency;
    }
}
