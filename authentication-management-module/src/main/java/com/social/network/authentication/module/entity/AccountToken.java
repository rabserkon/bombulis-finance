package com.social.network.authentication.module.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "token_at_sr_v1")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class AccountToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TOKEN_ID")
    private long tokenId;
    @Column(nullable = false, updatable = false)
    private String aboutToken;
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;
    @Column
    private LocalDateTime executionDate;
    @Column
    private boolean status;
    @Column(nullable = false, updatable = false, unique = true)
    private String tokenUUID;
    @Column(nullable = false, updatable = false)
    private LocalDateTime validateTime;;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false, updatable = false )
    private User user;
}
