package com.social.network.authentication.module.service.NotificationService.NotificationFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

public enum NotificationServiceType {
    EMAIL("email"),
    SMS("sms"),
    TELEGRAM("telegram");

    @Getter
    private final String serviceName;

    NotificationServiceType(String service) {
        this.serviceName= service;
    }

    @JsonValue
    public String getServiceName() {
        return serviceName;
    }

    @JsonCreator
    public static NotificationServiceType forValue(String value) {
        for (NotificationServiceType type : values()) {
            if (type.getServiceName().equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown enum value: " + value);
    }
}
