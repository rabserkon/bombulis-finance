package com.bombulis.accounting.entity;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
@NoArgsConstructor
public class WithdrawalDestination extends CurrencyAccount {

    public WithdrawalDestination(String name, Currency currency, User user) {
        super(name, currency, user);
    }
}
