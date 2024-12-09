package com.gateway.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class SessionInfo {
    private final long serialVersionUID = 1L;
    private String sessionUUID;
    private Date createTime;
    private Date updateTime;
    private boolean active;
    private Long userId;
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
