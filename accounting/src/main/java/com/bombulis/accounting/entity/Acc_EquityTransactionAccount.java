package com.bombulis.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@NoArgsConstructor
@Entity
@AllArgsConstructor
@Getter
@Setter
public class Acc_EquityTransactionAccount extends Acc_Transaction {

    @ManyToOne
    @JoinColumn(name = "currencyOperation")
    private Acc_Currency currencyOperation;

    private Long brokerAccountId;

    private Long equityAccount;
}
