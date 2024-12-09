package com.bombulis.accounting.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Entity
@NoArgsConstructor
public class Acc_Assets extends Acc_Account {
    public Acc_Assets(String name, Acc_User user) {
        super(name, user);
    }
}
