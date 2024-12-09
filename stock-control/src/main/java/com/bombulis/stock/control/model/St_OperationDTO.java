package com.bombulis.stock.control.model;

import com.bombulis.stock.control.component.validation.St_OperationValidType;
import com.bombulis.stock.control.entity.St_Operation;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class St_OperationDTO {
    @NotNull
    @NotBlank
    private String tiker;
    @NotNull
    @NotBlank
    private String isin;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @NotNull
    @Min(0)
    @Max(1000000)
    private Integer quantity;
    @NotNull
    @NotBlank
    @St_OperationValidType
    private String type;
    @NotNull
    private Long brokerAccountId;
    private LocalDateTime date;
    @NotNull
    private Long currencyAccountId;
    private BigDecimal amount;
    private String status;
    public St_OperationDTO(St_Operation operation) {
        this.amount = operation.getAmount();
        this.price = operation.getPrice();
        this.quantity = operation.getQuantity();
        this.type = operation.getType();
        this.status = operation.getStatus();
        this.brokerAccountId = operation.getBroker().getBrokerAccountId();
        this.date = operation.getDate();
        this.currencyAccountId = operation.getCurrencyAccountId();
    }
}
