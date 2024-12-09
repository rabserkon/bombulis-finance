package com.social.network.authentication.module.service.NotificationService.NotificationFactory;

import com.social.network.authentication.module.service.NotificationService.NotificationEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("email")
public class EmailNotificationService implements NotificationService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void sendNotification(NotificationEvent notification) {
        logger.info("Sending push notification to " + notification.getUser().getEmail() + " with message: " + notification.getMessage());
    }
}
