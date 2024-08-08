package com.bombulis.accounting.model.dao;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Account {
    private String name;
    private String currency;
    private String description;
    private String type;
    private String subType;
}
