package com.bombulis.accounting.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
public class Acc_WithdrawalDestination extends Acc_CurrencyAccount {

    public Acc_WithdrawalDestination(String name, Acc_Currency currency, Acc_User user) {
        super(name, currency, user);
    }
}
