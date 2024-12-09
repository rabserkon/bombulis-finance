package com.bombulis.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Acc_EquityTransactionDTO extends Acc_TransactionDTO {
    private String currencyCode;
    private Long currencyAccountId;
    private Long brokerAccountId;
    private Long equityAccountId;

    @Override
    public String toString() {
        return "Acc_StockTransactionDTO{" +
                "currencyCode='" + currencyCode + '\'' +
                ", currencyAccountId=" + currencyAccountId +
                ", brokerAccountId=" + brokerAccountId +
                ", equityAccountId=" + equityAccountId +
                '}';
    }
}

