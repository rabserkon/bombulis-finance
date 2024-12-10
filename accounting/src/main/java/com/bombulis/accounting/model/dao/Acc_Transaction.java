package com.bombulis.accounting.model.dao;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Acc_Transaction {
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
