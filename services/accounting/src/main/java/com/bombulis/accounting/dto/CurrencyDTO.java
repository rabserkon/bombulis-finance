package com.bombulis.accounting.dto;

import com.bombulis.accounting.entity.Currency;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyDTO {

    private Long id;
    private String fullName;
    private String isoCode;
    private String numericCode;

    public CurrencyDTO(Currency currency) {
        this.id = currency.getId();
        this.fullName = currency.getFullName();
        this.isoCode = currency.getIsoCode();
        this.numericCode = currency.getNumericCode();
    }
}
