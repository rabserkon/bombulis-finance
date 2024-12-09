package com.social.network.authentication.module.service.NotificationService;

import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TokenNotificationEvent extends NotificationEvent{
    protected String aboutToken;
    protected String token;

    public TokenNotificationEvent(User user, String subject, String message, NotificationServiceType service, String token) {
        super(user,subject, message, service);
        this.token = token;
    }

    public TokenNotificationEvent(String token) {
        this.token = token;
    }

    public TokenNotificationEvent() {
        super();
    }
}
