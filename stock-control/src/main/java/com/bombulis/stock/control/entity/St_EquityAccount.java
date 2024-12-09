package com.bombulis.stock.control.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("STOCK")
@Getter @Setter
public class St_EquityAccount extends St_Account{

    @Column(name = "s_ticker", length = 10)
    private String sTicker;

    public St_EquityAccount() {
        super();
    }
}
