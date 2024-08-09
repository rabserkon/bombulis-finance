package com.bombulis.financereport.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class Account {
    private Long accountId;
    private String name;
    private String currency;
    private String description;
    private String type;
    private String subType;
}
