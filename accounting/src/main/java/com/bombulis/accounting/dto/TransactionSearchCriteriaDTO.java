package com.bombulis.accounting.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

public class TransactionSearchCriteriaDTO {
    @Getter @Setter
    private LocalDate startDate;
    @Getter @Setter
    private LocalDate endDate;
    @Getter @Setter
    private long accountId;

    // Конструкторы
    public TransactionSearchCriteriaDTO(Long userId) {
        this.startDate = LocalDate.now().minusMonths(1);
        this.endDate = LocalDate.now();
    }
}
