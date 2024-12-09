package com.bombulis.financereport.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Rps_Transaction {
    private Long id;
    private Long senderAccountId;
    private Long receivedAccountId;
    private String description;
    private String type;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime transactionDate;
    private BigDecimal sendAmount;
    private BigDecimal receivedAmount;
    private BigDecimal exchangeRate;
}
