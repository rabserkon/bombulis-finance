package com.bombulis.financereport.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Rps_ConsolidateAccount {
    private Long accountId;
    private BigDecimal totalReceiveAmount;
    private BigDecimal totalSendAmount;
    private BigDecimal balanceBeforeStartPeriod;
    private BigDecimal balanceAfterEndPeriod;
}
