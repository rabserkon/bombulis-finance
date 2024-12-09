package com.social.network.authentication.module.service.NotificationService;

import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationEvent {
    protected User user;
    protected String subject;
    protected String message;
    protected NotificationServiceType service;


}
