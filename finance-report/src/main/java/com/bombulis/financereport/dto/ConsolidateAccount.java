package com.bombulis.financereport.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ConsolidateAccount {
    private Long accountId;
    private BigDecimal totalReceiveAmount;
    private BigDecimal totalSendAmount;
    private LocalDateTime balanceBeforeStartPeriod;
    private LocalDateTime balanceAfterEndPeriod;
}
