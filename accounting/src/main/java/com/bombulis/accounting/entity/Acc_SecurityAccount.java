package com.bombulis.accounting.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
public class Acc_SecurityAccount extends Acc_Assets {

    @Getter @Setter
    private String symbol;

    @Getter @Setter
    private String isin;

    @Getter @Setter
    private int quantity;

    public Acc_SecurityAccount(String name, Acc_User user, String symbol, String isin) {
        super(name, user);
        this.symbol = symbol;
        this.isin = isin;
        this.quantity = 0;
    }

    public Acc_SecurityAccount() {
        super();
    }
}
