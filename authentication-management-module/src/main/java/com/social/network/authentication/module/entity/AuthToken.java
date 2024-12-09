package com.social.network.authentication.module.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "AuthToken")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "about_token", nullable = false, updatable = false)
    private String aboutToken;

    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "execution_date_time")
    private LocalDateTime executionDate;

    @Column(name = "status_token", nullable = false)
    private Boolean status;

    @Column(name = "token_UUID", nullable = false, unique = true)
    private String tokenUUID;

    @Column(name = "validate_time", nullable = false, updatable = false)
    private LocalDateTime validateTime;

    @Column(name = "code_token", nullable = false, updatable = false)
    private String code;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    @Transient
    private String createCode;
}
