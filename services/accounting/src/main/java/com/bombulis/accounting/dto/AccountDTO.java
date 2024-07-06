package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Account;
import com.bombulis.accounting.model.ValidAccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    @NotNull
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String currency;
    @Getter @Setter
    private String description;
    @Getter @Setter
    @ValidAccountType
    private String type;


    public AccountDTO(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public AccountDTO(Account account) {
        this.name = account.getName();
        this.description = account.getDescription();
        this.type = account.getType();
    }
}
