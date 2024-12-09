package com.bombulis.accounting.model.dao;
import com.bombulis.accounting.entity.Acc_CurrencyAccount;
import lombok.*;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acc_Account {
    private Long accountId;
    private String name;
    private String currency;
    private String description;
    private String type;
    private String subType;

    public Acc_Account(Acc_CurrencyAccount account) {
        this.accountId = account.getId();
        this.name = account.getName();
        this.currency = account.getCurrency().getIsoCode();
        this.description = account.getDescription();
        this.type = account.getType();
        this.subType = account.getSubAccount();
    }
}
