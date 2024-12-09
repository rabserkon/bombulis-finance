package com.social.network.authentication.module.event;

import com.social.network.authentication.module.entity.User;
import org.springframework.context.ApplicationEvent;

import java.time.LocalDateTime;

public class EnabledConfirmEvent extends ApplicationEvent {
    private final User user;
    private final LocalDateTime activationTime;

    public EnabledConfirmEvent(User user) {
        super(user);
        this.user = user;
        this.activationTime = LocalDateTime.now();
    }

    public User getUser() {
        return user;
    }

    public LocalDateTime getActivationTime() {
        return activationTime;
    }
}
