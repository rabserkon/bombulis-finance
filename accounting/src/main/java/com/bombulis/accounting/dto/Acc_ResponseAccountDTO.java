package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Acc_ResponseAccountDTO {
    private Long id;
    private String name;
    private Timestamp create_at;
    private Timestamp lastUpdate_at;
    private Boolean archive;
    private String type;
    private String description;
    private Long userId;
    private Acc_CurrencyDTO currency;
    private BigDecimal balance;
    private Acc_BalanceDTO revaluationBalance;
    private String subType;

    public Acc_ResponseAccountDTO(Acc_Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.create_at = account.getCreate_at();
        this.lastUpdate_at = account.getLastUpdate_at();
        this.archive = account.isArchive();
        this.type = account.getType();
        this.description = account.getDescription();
        //this.userId = account.getUser().getUserId();
        if (account instanceof Acc_CurrencyAccount){
            Acc_CurrencyAccount currencyAccount = ((Acc_CurrencyAccount) account);
            this.currency = new Acc_CurrencyDTO(currencyAccount.getCurrency());
            this.balance = currencyAccount.getBalance();
            this.revaluationBalance = currencyAccount.getRevaluationBalance();
            this.subType = currencyAccount.getSubAccount();
        }


    }
}
