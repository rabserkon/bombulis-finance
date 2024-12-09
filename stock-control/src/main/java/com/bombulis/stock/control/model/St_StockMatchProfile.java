package com.bombulis.stock.control.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter@Setter
@AllArgsConstructor
@NoArgsConstructor
public class St_StockMatchProfile {
    private String symbol;
    private double price;
    private double beta;
    private long volAvg;
    private long mktCap;
    private double lastDiv;
    private String range;
    private double changes;
    private String companyName;
    private String currency;
    private String cik;
    private String isin;
    private String cusip;
    private String exchange;
    private String exchangeShortName;
    private String industry;
    private String website;
    private String description;
    private String ceo;
    private String sector;
    private String country;
    private String fullTimeEmployees;
    private String phone;
    private String address;
    private String city;
    private String state;
    private String zip;
    private double dcfDiff;
    private double dcf;
    private String image;
    private String ipoDate;
    private boolean defaultImage;
    private boolean isEtf;
    private boolean isActivelyTrading;
    private boolean isAdr;
    private boolean isFund;

}
