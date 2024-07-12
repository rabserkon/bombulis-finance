package com.bombulis.accounting.service.NasdaqApi;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class Status {
    @Getter
    @Setter
    private int rCode;
    @Getter
    @Setter
    private String bCodeMessage;
    @Getter
    @Setter
    private String developerMessage;

}
