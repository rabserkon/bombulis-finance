package com.bombulis.stock.control.model;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class St_CurrencyAccount {
    private Long accountId;
    private String name;
    private Timestamp create_at;
    private Timestamp lastUpdate_at;
    private boolean archive;
    private String type;
    private String description;
    private BigDecimal balance;
    private String isoCode;
}
