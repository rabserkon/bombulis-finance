package com.bombulis.accounting.model.dao;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConsolidateAccount {
    private Long accountId;
    private BigDecimal totalReceiveAmount;
    private BigDecimal totalSendAmount;
    private BigDecimal balanceBeforeStartPeriod;
    private BigDecimal balanceAfterEndPeriod;
}
