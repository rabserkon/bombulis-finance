package com.bombulis.accounting.dto;

import com.bombulis.accounting.model.ValidTransactionType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    @NotNull
    @Getter @Setter
    private Long senderAccountId;
    @NotNull
    @Getter @Setter
    private Long receivedAccountId;
    @Getter @Setter
    private String description;
    @NotNull
    @Getter @Setter
    @ValidTransactionType
    private String type;
    @NotNull
    @Getter @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private Date transactionDate;
    @NotNull
    @Getter @Setter
    private BigDecimal sendAmount;
    @Getter @Setter
    private BigDecimal receivedAmount;
    @Getter @Setter
    private BigDecimal exchangeRate;
}
