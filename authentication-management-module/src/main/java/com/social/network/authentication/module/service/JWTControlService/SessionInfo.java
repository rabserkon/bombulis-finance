package com.social.network.authentication.module.service.JWTControlService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public class SessionInfo {
    @Getter @Setter
    private final long serialVersionUID = 1L;
    @Getter @Setter
    private String sessionUUID;
    @Getter @Setter
    private Date createTime;
    @Getter @Setter
    private Date updateTime;
    @Getter @Setter
    private boolean active;
    @Getter @Setter
    private Long userId;
    @Getter@Setter
    private String device;

    public SessionInfo(String sessionUUID, long userId) {
        this.sessionUUID = sessionUUID;
        this.createTime = new Date();
        this.updateTime = new Date();
        this.active = true;
        this.userId = userId;
    }

    public SessionInfo(String sessionUUID, long userId, String device) {
        this.sessionUUID = sessionUUID;
        this.createTime = new Date();
        this.updateTime = new Date();
        this.active = true;
        this.userId = userId;
        this.device = device;
    }
}
