package org.nasduq.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PrimaryData {
    @Getter @Setter
    private String lastSalePrice;
    @Getter @Setter
    private String netChange;
    @Getter @Setter
    private String percentageChange;
    @Getter @Setter
    private String deltaIndicator;
    @Getter @Setter
    private String lastTradeTimestamp;
    @Getter @Setter
    private boolean isRealTime;
}
