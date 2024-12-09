package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Acc_Account;
import com.bombulis.accounting.model.Acc_ValidAccountType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
public class Acc_SourceDTO {
    @NotNull
    @Getter @Setter
    private String name;
    @Getter @Setter
    private String currency;
    @Getter @Setter
    private String description;
    @Getter @Setter
    @Acc_ValidAccountType
    private String type;


    public Acc_SourceDTO(String name, String description, String type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    public Acc_SourceDTO(Acc_Account account) {
        this.name = account.getName();
        this.description = account.getDescription();
        this.type = account.getType();
    }
}