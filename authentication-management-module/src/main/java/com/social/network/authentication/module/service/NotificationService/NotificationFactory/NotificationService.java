package com.social.network.authentication.module.service.NotificationService.NotificationFactory;

import com.social.network.authentication.module.service.NotificationService.NotificationEvent;

public interface NotificationService {
    void sendNotification(NotificationEvent notification);
}
