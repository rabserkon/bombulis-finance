package com.social.network.authentication.module.service.KafkaProducerService;

import com.social.network.authentication.module.service.KafkaProducerService.event.RegistrationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.registration}")
    private String registrationTopic;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRegistrationEvent(RegistrationEvent event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(registrationTopic, event);
    }
}
