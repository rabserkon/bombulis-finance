package com.social.network.authentication.module.service.BruteForceService;

import lombok.Getter;

import java.util.Date;

public class AttemptLogin {
    @Getter
    private final long userId;
    @Getter
    public final Date dateFailedLogin;
    @Getter
    public final String ipFailedLogin;
    @Getter
    public final String browserData;
    @Getter
    public final String method;

    public AttemptLogin(long userId, Date dateFailedLogin, String ipFailedLogin, String browserData, String method) {
        this.userId = userId;
        this.dateFailedLogin = dateFailedLogin;
        this.ipFailedLogin = ipFailedLogin;
        this.browserData = browserData;
        this.method = method;
    }

    @Override
    public String toString() {
        return "AttemptLogin{" +
                "userId=" + userId +
                ", dateFailedLogin=" + dateFailedLogin +
                ", ipFailedLogin='" + ipFailedLogin + '\'' +
                ", browserData='" + browserData + '\'' +
                ", method='" + method + '\'' +
                '}';
    }
}

