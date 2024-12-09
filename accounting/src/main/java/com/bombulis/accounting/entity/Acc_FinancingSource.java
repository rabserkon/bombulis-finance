package com.bombulis.accounting.entity;


import javax.persistence.*;

@Entity
@Table
public class Acc_FinancingSource extends Acc_CurrencyAccount {

    public Acc_FinancingSource(String name, Acc_Currency currency, Acc_User user) {
        super(name, currency, user);
    }

    public Acc_FinancingSource() {
        super();
    }
}
