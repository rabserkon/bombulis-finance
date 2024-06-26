package com.bombulis.accounting.component;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {
    @Getter
    @Setter
    private long id;
    @Getter @Setter
    private String name;
    @Getter
    @Setter
    private long userId;

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userId=" + userId +
                '}';
    }

    @Override
    public String getAuthority() {
        return name;
    }
}
