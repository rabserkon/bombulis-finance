package com.bombulis.accounting.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table
public class FinancingSource extends CurrencyAccount{

    public FinancingSource(String name, Currency currency, User user) {
        super(name, currency, user);
    }

    public FinancingSource() {
        super();
    }
}
