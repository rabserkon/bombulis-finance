package com.social.network.authentication.module.service.NotificationService;

import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationService;
import com.social.network.authentication.module.service.NotificationService.NotificationFactory.NotificationServiceType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class NotificationServiceFactory implements NotificationSenderService {
    private final Map<String, NotificationService> services;

    @Autowired
    public NotificationServiceFactory(Map<String, NotificationService> services) {
        this.services = services;
    }

    @Override
    public void sendNotification(NotificationEvent event) {
        if(event.getService() == null){
            throw new IllegalArgumentException("Notification don't have service type" );
        }
        NotificationService service = services.get(event.getService().getServiceName());
        if (service == null) {
            throw new IllegalArgumentException("No notification service found for type: " + event.getService().getServiceName());
        }
        service.sendNotification(event);
    }

    @Override
    public void sendNotification(NotificationEvent event, NotificationServiceType serviceType) {
        NotificationService service = services.get(serviceType.getServiceName().toLowerCase());
        if (service == null) {
            throw new IllegalArgumentException("No notification service found for type: " + serviceType.getServiceName());
        }
        service.sendNotification(event);
    }
}
