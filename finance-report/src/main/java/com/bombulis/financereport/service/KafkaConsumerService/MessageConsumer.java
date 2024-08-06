package com.bombulis.financereport.service.KafkaConsumerService;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    @KafkaListener(topics = "reports-topic", groupId = "reports-group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
}
