package com.bombulis.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
public class AccountEditDTO {
    @NotNull
    @Size(min = 0, max = 15, message = "Account name size must be between 0 and 15")
    @Getter @Setter
    private String name;
    @Getter @Setter
    private boolean archive;
    @Getter @Setter
    @Size(min = 0, max = 200)
    private String description;
    @NotNull
    @Getter @Setter
    private Long id;
}
