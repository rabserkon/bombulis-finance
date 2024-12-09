package com.bombulis.accounting.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Table(name = "role")
public class Acc_Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false, columnDefinition = "serial")
    private Long roleId;

    @Column(updatable = false, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "userId", updatable = false, nullable = false)
    private Acc_User user;

    public Acc_Role(String name, Acc_User user) {
        this.name = name;
        this.user = user;
    }

    @Override
    public String getAuthority() {
        return null;
    }
}
