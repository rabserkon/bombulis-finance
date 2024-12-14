package com.minecraftservers.app.service.KubernetesService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    protected void sendNotification(String message) {
        logger.info("Notification: " + message);
    }
}
