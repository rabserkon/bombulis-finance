package com.bombulis.accounting.model.dao;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@ToString
public class Transaction {
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
