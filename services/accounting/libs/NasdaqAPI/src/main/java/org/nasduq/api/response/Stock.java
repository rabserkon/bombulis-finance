package org.nasduq.api.response;

import lombok.Getter;
import lombok.Setter;

public class Stock {
    @Getter @Setter
    private StockInformation data;
    @Getter @Setter
    private String message;
    @Getter @Setter
    private Status status;

    @Override
    public String toString() {
        return "Stock{" +
                "data=" + data.toString() +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
