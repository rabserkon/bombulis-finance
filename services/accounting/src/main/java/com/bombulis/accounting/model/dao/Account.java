package com.bombulis.accounting.model.dao;
import lombok.*;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    private Long accountId;
    private String name;
    private String currency;
    private String description;
    private String type;
    private String subType;

    public Account(com.bombulis.accounting.entity.CurrencyAccount account) {
        this.accountId = account.getId();
        this.name = account.getName();
        this.currency = account.getCurrency().getIsoCode();
        this.description = account.getDescription();
        this.type = account.getType();
        this.subType = account.getSubAccount();
    }
}
