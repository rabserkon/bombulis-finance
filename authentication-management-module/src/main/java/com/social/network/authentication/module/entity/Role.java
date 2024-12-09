package com.social.network.authentication.module.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity
@Table(name = "role_at_sr_v1")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long roleId;
    @Getter @Setter
    @Column(name = "role_name")
    private String roleName;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
