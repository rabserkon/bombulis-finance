package com.bombulis.stock.control.service.AuthenticationService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
public final class St_SessionInfo implements Serializable {
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


    public St_SessionInfo(String sessionUUID, long userId, String device) {
        this.sessionUUID = sessionUUID;
        this.createTime = new Date();
        this.updateTime = new Date();
        this.active = true;
        this.userId = userId;
        this.device = device;
    }
}
