package com.bombulis.stock.control.component;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@AllArgsConstructor
@ToString
public class St_Role implements GrantedAuthority {
    @Getter @Setter
    private String name;

    @Override
    public String getAuthority() {
        return name;
    }
}
