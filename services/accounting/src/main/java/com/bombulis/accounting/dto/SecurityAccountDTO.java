package com.bombulis.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
public class SecurityAccountDTO extends AccountDTO{
    @NotNull
    @Getter
    @Setter
    private String name;
    @NotNull
    @Getter @Setter
    private String ticker;
    private String type;


    public SecurityAccountDTO(@NotNull String name, @NotNull String currency, @NotNull String description, String name1, String ticker, String type) {
        super(name, description, type);
        this.name = name1;
        this.ticker = ticker;
    }


}
