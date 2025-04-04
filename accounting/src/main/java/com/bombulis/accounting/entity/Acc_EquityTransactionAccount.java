package com.bombulis.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.*;

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
