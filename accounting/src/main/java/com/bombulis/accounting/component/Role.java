package com.bombulis.accounting.component;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Role implements GrantedAuthority {
    @Getter @Setter
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
