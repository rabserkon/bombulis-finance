package com.bombulis.accounting.entity;

import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
@NoArgsConstructor
public class Assets extends Account{
    public Assets(String name, User user) {
        super(name, user);
    }
}
