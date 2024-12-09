package com.social.network.authentication.module.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_at_sr_v1")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "account_non_locked", nullable = false)
    private boolean accountNonLocked;

    @Column(name = "date_update_email")
    private LocalDateTime dateUpdateEmail;

    @Column(name = "email",unique = true, nullable = false, length = 120)
    private String email;

    @Column(name = "enabled", nullable = false)
    private boolean enabled;

    @Column(name = "first_email", nullable = false)
    private String firstEmail;

    @Column(name = "last_email")
    private String lastEmail;

    @Column(name = "login", unique = true, nullable = false, length = 36)
    private String login;

    @Column(name = "password", nullable = false, length = 128)
    private String password;

    @Column(name = "send_code_to_telegram", nullable = false)
    private boolean sendCodeToTelegram;

    @Column(name = "telegram_chat_id", unique = true)
    private String telegramChatId;

    @Column(name = "uuid", nullable = false, unique = true, length = 36)
    private String uuid;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<AuthToken> authTokens;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Role> roleList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<AccountToken> accountTokenList;

}
