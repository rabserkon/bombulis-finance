package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.entity.CurrencyAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class ResponseAccountDTO {
    private Long id;
    private String name;
    private Timestamp create_at;
    private Timestamp lastUpdate_at;
    private Boolean archive;
    private String type;
    private String description;
    private Long userId;
    private CurrencyDTO currency;
    private BigDecimal balance;
    private BalanceDTO revaluationBalance;
    private String subType;

    public ResponseAccountDTO(Account account) {
        this.id = account.getId();
        this.name = account.getName();
        this.create_at = account.getCreate_at();
        this.lastUpdate_at = account.getLastUpdate_at();
        this.archive = account.isArchive();
        this.type = account.getType();
        this.description = account.getDescription();
        //this.userId = account.getUser().getUserId();
        if (account instanceof CurrencyAccount){
            CurrencyAccount currencyAccount = ((CurrencyAccount) account);
            this.currency = new CurrencyDTO(currencyAccount.getCurrency());
            this.balance = currencyAccount.getBalance();
            this.revaluationBalance = currencyAccount.getRevaluationBalance();
            this.subType = currencyAccount.getSubAccount();
        }


    }
}
