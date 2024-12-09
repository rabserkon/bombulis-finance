package com.social.network.authentication.module.service.KafkaProducerService.event;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegistrationEvent {
    private Long userId;
    private String version = "1.0";
}
