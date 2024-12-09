package com.bombulis.stock.control.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("BOND")
public class St_BondAccount extends St_Account{

    @Column(name = "s_face_value")
    private BigDecimal sFaceValue; // Номинальная стоимость облигации

    @Column(name = "s_coupon_rate")
    private BigDecimal sCouponRate; // Процентная ставка

    @Column(name = "s_maturity_date")
    private LocalDateTime sMaturityDate; // Дата погашения

    @Column(name = "s_bond_type")
    private String sBondType; // Тип облигации


    public St_BondAccount() {
        super();
    }
}
