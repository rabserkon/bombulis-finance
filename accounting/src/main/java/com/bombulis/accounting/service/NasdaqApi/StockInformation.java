package com.bombulis.accounting.service.NasdaqApi;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
public class StockInformation {
    @JsonProperty("symbol")
    @Getter @Setter
    private String symbol;

    @JsonProperty("companyName")
    @Getter @Setter
    private String companyName;

    @JsonProperty("stockType")
    @Getter @Setter
    private String stockType;

    @JsonProperty("exchange")
    @Getter @Setter
    private String exchange;

    @JsonProperty("isNasdaqListed")
    @Getter @Setter
    private boolean isNasdaqListed;

    @JsonProperty("isNasdaq100")
    @Getter @Setter
    private boolean isNasdaq100;

    @JsonProperty("isHeld")
    @Getter @Setter
    private boolean isHeld;

    @JsonProperty("secondaryData")
    @Getter @Setter
    private Object secondaryData;

    @JsonProperty("marketStatus")
    @Getter @Setter
    private String marketStatus;

    @JsonProperty("assetClass")
    @Getter @Setter
    private String assetClass;
}
