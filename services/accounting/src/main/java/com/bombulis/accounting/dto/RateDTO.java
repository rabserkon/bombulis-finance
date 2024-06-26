package com.bombulis.accounting.dto;


import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@ToString
public class RateDTO {
    @Getter @Setter
    private String fullName;
    @Getter @Setter
    private String ticker;
    @Getter @Setter
    private String mainCurrency;
    @Getter @Setter
    private double exchangeRate;
}
