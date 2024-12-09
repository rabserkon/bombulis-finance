package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Acc_Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Acc_CurrencyDTO {

    private Long id;
    private String fullName;
    private String isoCode;
    private String numericCode;

    public Acc_CurrencyDTO(Acc_Currency currency) {
        this.id = currency.getId();
        this.fullName = currency.getFullName();
        this.isoCode = currency.getIsoCode();
        this.numericCode = currency.getNumericCode();
    }
}
