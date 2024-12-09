package com.social.network.authentication.module.service.KafkaProducerService;

import com.social.network.authentication.module.service.KafkaProducerService.event.RegistrationEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

@Service
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${kafka.topic.registration}")
    private String registrationTopic;

    public KafkaProducerService(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendRegistrationEvent(RegistrationEvent event) {
        ListenableFuture<SendResult<String, Object>> future = kafkaTemplate.send(registrationTopic, event);
        future.addCallback(
                result -> {
                    // write your Successful send logic here
                },
                ex -> {
                    // write your customer error handling logic here
                });
    }
}
