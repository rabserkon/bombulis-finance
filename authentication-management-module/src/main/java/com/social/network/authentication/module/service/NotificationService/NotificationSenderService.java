package com.social.network.authentication.module.service.NotificationService;

import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;

public interface NotificationSenderService {
    void sendNotification(NotificationEvent event);

    void sendNotification(NotificationEvent event, NotificationServiceType serviceType);
}
